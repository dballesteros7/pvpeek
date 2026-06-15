package com.pogopvp.overlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Color
import android.graphics.PixelFormat
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlin.math.hypot

/**
 * Persistent floating Pokéball. It lives on the screen edge as a foreground service. Tapping
 * it captures one frame and analyses the Pokémon; the first tap requests screen-capture
 * consent and the projection is then kept for the whole session. Drag to move, long-press to
 * dismiss.
 *
 * Foreground-service type is `specialUse` while idle (just the ball) and is promoted to
 * `mediaProjection` once the user grants screen capture — Android 14+ requires the
 * mediaProjection type to be present before creating the capture's VirtualDisplay.
 */
class OverlayService : Service() {

    companion object {
        private const val CHANNEL_ID = "pvp_overlay"
        private const val NOTIF_ID = 42
        private const val EXTRA_CODE = "result_code"
        private const val EXTRA_DATA = "result_data"
        private const val ACTION_PROJECTION_GRANTED = "projection_granted"
        private const val ACTION_PROJECTION_DENIED = "projection_denied"

        fun start(ctx: Context) {
            ContextCompat.startForegroundService(ctx, Intent(ctx, OverlayService::class.java))
        }

        fun deliverProjection(ctx: Context, resultCode: Int, data: Intent) {
            ContextCompat.startForegroundService(
                ctx,
                Intent(ctx, OverlayService::class.java)
                    .setAction(ACTION_PROJECTION_GRANTED)
                    .putExtra(EXTRA_CODE, resultCode)
                    .putExtra(EXTRA_DATA, data)
            )
        }

        fun projectionDenied(ctx: Context) {
            ContextCompat.startForegroundService(
                ctx, Intent(ctx, OverlayService::class.java).setAction(ACTION_PROJECTION_DENIED)
            )
        }
    }

    private val main = Handler(Looper.getMainLooper())
    private lateinit var windowManager: WindowManager
    private val scanner = OcrScanner()

    private var ball: ImageView? = null
    private var ballParams: WindowManager.LayoutParams? = null
    private var card: View? = null
    private var resultView: TextView? = null
    private var closeTarget: View? = null

    private var projection: MediaProjection? = null
    private var capture: ScreenCapture? = null
    private var scanning = false
    /** A tap is waiting for consent to come back before it can capture. */
    private var pendingScan = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        startForegroundCompat(idle = true)
        addBall()
        // Full dex + recommended moves off the main thread; until ready, Species falls back
        // to the mini-dex and moves simply don't show yet.
        Thread {
            Species.init(applicationContext)
            runCatching { Moves.init(applicationContext) }
        }.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PROJECTION_GRANTED -> {
                val code = intent.getIntExtra(EXTRA_CODE, android.app.Activity.RESULT_CANCELED)
                intentData(intent)?.let { handleProjectionGranted(code, it) }
            }
            ACTION_PROJECTION_DENIED -> {
                pendingScan = false
                setBallBusy(false)
                toast("Screen capture needed to scan")
            }
        }
        return START_NOT_STICKY
    }

    // --- Pokéball interactions --------------------------------------------

    private fun onBallTapped() {
        if (scanning) return
        if (capture != null) {
            doCapture()
        } else {
            // First tap: go get screen-capture consent, then auto-capture.
            pendingScan = true
            setBallBusy(true)
            ProjectionRequestActivity.launch(this)
        }
    }

    private fun handleProjectionGranted(code: Int, data: Intent) {
        try {
            // Android 14: the mediaProjection FGS type must be active BEFORE touching the
            // MediaProjection APIs, so promote the service first.
            startForegroundCompat(idle = false)
            val mpm = getSystemService(MediaProjectionManager::class.java)
            val mp = mpm.getMediaProjection(code, data)
            mp.registerCallback(object : MediaProjection.Callback() {
                override fun onStop() = stopSelf()
            }, main)
            projection = mp
            capture = ScreenCapture(this, mp)
            if (pendingScan) {
                pendingScan = false
                doCapture()
            }
        } catch (t: Throwable) {
            pendingScan = false
            setBallBusy(false)
            reportCrash("projection", t)
        }
    }

    private fun doCapture() {
        val cap = capture ?: return
        if (scanning) return
        scanning = true
        setBallBusy(true)
        cap.captureFrame { bitmap ->
            try {
                if (bitmap == null) {
                    finishScan("Capture failed — tap again")
                    Analytics.logAppraise(applicationContext, "capture_failed", "none", 0, false, "unknown", null)
                    return@captureFrame
                }
                val profile = DeviceProfiles.forBitmap(bitmap.width, bitmap.height)
                val frame = frameTag(bitmap.width, bitmap.height)
                val iv = AppraisalScanner.read(bitmap, profile)
                scanner.scan(bitmap, profile) { scan ->
                    try {
                        bitmap.recycle()
                        val base = scan.name?.let { Species.match(it, scan.types) }
                        finishScan(Analyzer.summarizeFull(scan, iv))
                        Analytics.logAppraise(
                            context = applicationContext,
                            outcome = if (base != null) "ok" else "name_unrecognized",
                            nameSource = scan.nameSource,
                            typesDetected = scan.types.size,
                            cpRead = scan.cp != null,
                            frame = frame,
                            species = scan.name
                        )
                    } catch (t: Throwable) {
                        reportCrash("analyze", t)
                    }
                }
            } catch (t: Throwable) {
                reportCrash("read", t)
            }
        }
    }

    private fun finishScan(text: String) {
        scanning = false
        main.post {
            setBallBusy(false)
            showCard(text)
        }
    }

    private fun setBallBusy(busy: Boolean) = main.post {
        ball?.alpha = if (busy) 0.45f else 0.9f
    }

    // --- Views -------------------------------------------------------------

    private fun addBall() {
        val sizePx = (resources.displayMetrics.density * 56).toInt()
        val view = ImageView(this).apply {
            setImageResource(R.drawable.ic_launcher)
            alpha = 0.9f
        }
        val params = overlayParams(sizePx, sizePx).apply {
            gravity = Gravity.TOP or Gravity.START
            x = resources.displayMetrics.widthPixels - sizePx
            y = resources.displayMetrics.heightPixels / 3
        }
        attachBallTouch(view, params)
        windowManager.addView(view, params)
        ball = view
        ballParams = params
    }

    private fun showCard(text: String) {
        val existing = resultView
        if (existing != null) {
            existing.text = text
            return
        }
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.argb(235, 20, 20, 24))
            setPadding(28, 24, 28, 24)
        }
        val tv = TextView(this).apply {
            this.text = text
            setTextColor(Color.WHITE)
            textSize = 13f
            width = (resources.displayMetrics.density * 200).toInt()
        }
        val hint = TextView(this).apply {
            this.text = "tap to dismiss"
            setTextColor(Color.argb(150, 255, 255, 255))
            textSize = 10f
        }
        container.addView(tv)
        container.addView(hint)
        container.setOnClickListener { removeCard() }

        val params = overlayParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            val dm = resources.displayMetrics
            val ballSize = (dm.density * 56).toInt()
            val margin = (dm.density * 8).toInt()
            val cardW = (dm.density * 230).toInt()
            val estCardH = (dm.density * 150).toInt()
            val bx = ballParams?.x ?: margin
            val by = ballParams?.y ?: 300
            x = bx.coerceIn(margin, (dm.widthPixels - cardW - margin).coerceAtLeast(margin))
            // Below the ball, or above it if there isn't room — never on top of it.
            val belowY = by + ballSize + margin
            y = if (belowY + estCardH <= dm.heightPixels) belowY
            else (by - estCardH - margin).coerceAtLeast(margin)
        }
        windowManager.addView(container, params)
        card = container
        resultView = tv
    }

    private fun removeCard() {
        card?.let { runCatching { windowManager.removeView(it) } }
        card = null
        resultView = null
    }

    private fun overlayParams(w: Int, h: Int) = WindowManager.LayoutParams(
        w, h,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )

    private fun attachBallTouch(view: View, params: WindowManager.LayoutParams) {
        var downX = 0f
        var downY = 0f
        var startX = 0
        var startY = 0
        var dragging = false
        val slop = resources.displayMetrics.density * 12
        view.setOnTouchListener { v, e ->
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    downX = e.rawX; downY = e.rawY
                    startX = params.x; startY = params.y
                    dragging = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = e.rawX - downX
                    val dy = e.rawY - downY
                    if (!dragging && hypot(dx, dy) > slop) {
                        dragging = true
                        showCloseTarget()
                    }
                    if (dragging) {
                        params.x = startX + dx.toInt()
                        params.y = startY + dy.toInt()
                        runCatching { windowManager.updateViewLayout(v, params) }
                        highlightCloseTarget(inCloseZone(e.rawY))
                    }
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (dragging) {
                        val close = inCloseZone(e.rawY)
                        hideCloseTarget()
                        if (close) quit() else snapToEdge(v, params)
                    } else {
                        onBallTapped()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun snapToEdge(view: View, params: WindowManager.LayoutParams) {
        val screenW = resources.displayMetrics.widthPixels
        params.x = if (params.x + view.width / 2 < screenW / 2) 0 else screenW - view.width
        runCatching { windowManager.updateViewLayout(view, params) }
    }

    /** Bottom band of the screen that dismisses the ball when released there. */
    private fun inCloseZone(rawY: Float): Boolean =
        rawY > resources.displayMetrics.heightPixels - resources.displayMetrics.density * 130

    private fun showCloseTarget() {
        if (closeTarget != null) return
        val size = (resources.displayMetrics.density * 64).toInt()
        val tv = TextView(this).apply {
            text = "✕"
            setTextColor(Color.WHITE)
            textSize = 26f
            gravity = Gravity.CENTER
            alpha = 0.7f
            setBackgroundColor(Color.argb(190, 200, 50, 50))
        }
        val p = overlayParams(size, size).apply {
            gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            y = (resources.displayMetrics.density * 48).toInt()
        }
        runCatching { windowManager.addView(tv, p); closeTarget = tv }
    }

    private fun highlightCloseTarget(active: Boolean) {
        closeTarget?.alpha = if (active) 1f else 0.7f
    }

    private fun hideCloseTarget() {
        closeTarget?.let { runCatching { windowManager.removeView(it) } }
        closeTarget = null
    }

    private fun quit() {
        toast("PvP overlay closed")
        stopSelf()
    }

    // --- Foreground plumbing ----------------------------------------------

    private fun startForegroundCompat(idle: Boolean) {
        val mgr = getSystemService(NotificationManager::class.java)
        mgr.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, "PvP Overlay", NotificationManager.IMPORTANCE_LOW)
        )
        val notif: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("PvP Overlay")
            .setContentText("Tap the Pokéball over a Pokémon to analyse it")
            .setSmallIcon(R.drawable.ic_launcher)
            .setOngoing(true)
            .build()
        runCatching {
            when {
                !idle && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                    startForeground(NOTIF_ID, notif, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION)
                idle && Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE ->
                    startForeground(NOTIF_ID, notif, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
                else -> startForeground(NOTIF_ID, notif)
            }
        }.onFailure { reportCrash("foreground", it) }
    }

    private fun reportCrash(where: String, t: Throwable) {
        runCatching {
            val fc = FirebaseCrashlytics.getInstance()
            fc.setCustomKey("stage", where)
            fc.recordException(t)
        }
        Log.e("PvpOverlay", "Failure in $where", t)
        scanning = false
        main.post { setBallBusy(false); showCard("ERROR ($where): ${t.javaClass.simpleName}") }
    }

    private fun toast(msg: String) = main.post {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    private fun frameTag(w: Int, h: Int): String = when {
        w == 1080 && h >= 2600 -> "flip5"
        w == 1080 && h in 2400..2599 -> "flip7"
        else -> "${w}x${h}"
    }

    override fun onDestroy() {
        super.onDestroy()
        ball?.let { runCatching { windowManager.removeView(it) } }
        removeCard()
        capture?.release()
        projection?.stop()
        scanner.close()
        NotificationManagerCompat.from(this).cancel(NOTIF_ID)
    }

    private fun intentData(intent: Intent): Intent? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(EXTRA_DATA, Intent::class.java)
        else
            @Suppress("DEPRECATION") intent.getParcelableExtra(EXTRA_DATA)
}
