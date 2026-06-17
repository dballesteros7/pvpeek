package com.pogopvp.overlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.PixelFormat
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
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
 * Persistent floating PvPeek button, hosted by a `mediaProjection` foreground service.
 * Screen-capture consent is obtained at launch (MainActivity) and the projection is kept for the
 * whole session; tapping the button captures one frame and analyses the Pokémon. Drag to move,
 * drag to the bottom of the screen to dismiss.
 */
class OverlayService : Service() {

    companion object {
        private const val CHANNEL_ID = "pvp_overlay"
        private const val NOTIF_ID = 42
        private const val EXTRA_CODE = "result_code"
        private const val EXTRA_DATA = "result_data"

        /** Started from MainActivity with the MediaProjection consent token (granted at launch). */
        fun start(ctx: Context, resultCode: Int, data: Intent) {
            ContextCompat.startForegroundService(
                ctx,
                Intent(ctx, OverlayService::class.java)
                    .putExtra(EXTRA_CODE, resultCode)
                    .putExtra(EXTRA_DATA, data)
            )
        }
    }

    private val main = Handler(Looper.getMainLooper())
    private lateinit var windowManager: WindowManager
    private val scanner = OcrScanner()

    private var ball: ImageView? = null
    private var ballParams: WindowManager.LayoutParams? = null
    private var card: View? = null
    private var closeTarget: View? = null

    private var projection: MediaProjection? = null
    private var capture: ScreenCapture? = null
    private var scanning = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Consent.apply(applicationContext) // honour analytics/crash choices
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        // Full dex + recommended moves off the main thread; until ready, Species falls back
        // to the mini-dex and moves simply don't show yet.
        Thread {
            Species.init(applicationContext)
            runCatching { Moves.init(applicationContext) }
        }.start()
    }

    /**
     * Started with the MediaProjection token obtained at launch. We immediately become a
     * `mediaProjection` foreground service, wire up capture, and show the floating button.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (projection == null && intent != null) {
            val code = intent.getIntExtra(EXTRA_CODE, android.app.Activity.RESULT_CANCELED)
            val data = intentData(intent)
            if (data != null) {
                try {
                    startForegroundCompat()
                    val mpm = getSystemService(MediaProjectionManager::class.java)
                    val mp = mpm.getMediaProjection(code, data)
                    mp.registerCallback(object : MediaProjection.Callback() {
                        override fun onStop() = stopSelf()
                    }, main)
                    projection = mp
                    capture = ScreenCapture(this, mp)
                    addBall()
                } catch (t: Throwable) {
                    reportCrash("projection", t)
                    stopSelf()
                }
            } else {
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    // --- PvPeek button interactions --------------------------------------------

    private fun onBallTapped() {
        if (scanning) return
        doCapture()
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
                        finishVerdict(Analyzer.analyze(scan, iv))
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

    private fun finishVerdict(verdict: Verdict) {
        scanning = false
        main.post {
            setBallBusy(false)
            showVerdictCard(verdict)
        }
    }

    private fun finishScan(text: String) {
        scanning = false
        main.post {
            setBallBusy(false)
            showCard(text)
        }
    }

    /** Dim and re-tint the button's gold findability ring to a blue "scanning" cue. */
    private fun setBallBusy(busy: Boolean) = main.post {
        ball?.apply {
            alpha = if (busy) 0.6f else 0.95f
            background = Brand.fabBackground(this@OverlayService, if (busy) Brand.blueBright else Brand.gold)
        }
    }

    // --- Views -------------------------------------------------------------

    private fun addBall() {
        val sizePx = (resources.displayMetrics.density * 56).toInt()
        val view = ImageView(this).apply {
            setImageResource(R.drawable.ic_mark)
            // Dark radial fill + gold ring = a findable HUD button over bright game art.
            background = Brand.fabBackground(this@OverlayService, Brand.gold)
            val p = Brand.dp(this@OverlayService, 9)
            setPadding(p, p, p, p)
            alpha = 0.95f
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

    /**
     * True when the platform can blur content behind a window (API 31+, and the user/device
     * hasn't disabled it). Lets the verdict card use the design system's lighter scrim +
     * backdrop blur instead of a heavy opaque scrim.
     */
    private fun blurBehindSupported(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && windowManager.isCrossWindowBlurEnabled

    /** A simple branded card for status / error messages (mono text + dismiss hint). */
    private fun showCard(text: String) {
        val blur = blurBehindSupported()
        val container = brandedCardContainer(blur)
        container.addView(TextView(this).apply {
            this.text = text
            typeface = Brand.mono(this@OverlayService, 500)
            setTextColor(Brand.textPrimary)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            setLineSpacing(0f, 1.35f)
            width = Brand.dp(this@OverlayService, 220)
        })
        container.addView(footerRow())
        attachCard(container, estHeightDp = 120, blur = blur)
    }

    /**
     * The hero verdict HUD: identity + CP, then the headline numbers (IV%, each league's
     * rank + percentile) in tabular mono, tier-coloured via [Brand.tierColor]. Floats over
     * the game on a strong scrim so it stays legible against arbitrary art.
     */
    private fun showVerdictCard(verdict: Verdict) {
        val blur = blurBehindSupported()
        val container = brandedCardContainer(blur)
        container.addView(verdictHeader(verdict))
        if (verdict.recognized) {
            container.addView(divider(topDp = 12, bottomDp = 12))
            container.addView(statRow(verdict))
            verdict.leagues.firstOrNull { it.moves != null }?.let { container.addView(movesLine(it)) }
        } else {
            container.addView(divider(topDp = 12, bottomDp = 10))
            container.addView(TextView(this).apply {
                text = "Name not recognised — re-tap on the Appraisal screen."
                typeface = Brand.body(this@OverlayService, 500)
                setTextColor(Brand.textSecondary)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            })
        }
        container.addView(divider(topDp = 12, bottomDp = 10))
        container.addView(footerRow())
        attachCard(container, estHeightDp = if (verdict.recognized) 220 else 150, blur = blur)
    }

    // --- Verdict card pieces ----------------------------------------------

    private fun brandedCardContainer(blur: Boolean): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        background = Brand.overlayCardBackground(this@OverlayService, blurred = blur)
        val ph = Brand.dp(this@OverlayService, 16)
        val pv = Brand.dp(this@OverlayService, 14)
        setPadding(ph, pv, ph, pv)
        elevation = Brand.dp(this@OverlayService, 12).toFloat()
        setOnClickListener { removeCard() }
    }

    private fun verdictHeader(v: Verdict): View {
        val ctx = this
        val logo = ImageView(this).apply {
            setImageResource(R.drawable.ic_mark)
            val s = Brand.dp(ctx, 26)
            layoutParams = LinearLayout.LayoutParams(s, s).apply { rightMargin = Brand.dp(ctx, 10) }
        }
        val nameRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(TextView(ctx).apply {
                text = v.name
                typeface = Brand.display(ctx, 700)
                setTextColor(Brand.textPrimary)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                maxLines = 1
                ellipsize = TextUtils.TruncateAt.END
            })
            if (v.isHundo) addView(hundoBadge())
        }
        val cpLine = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.BOTTOM
            v.cp?.let { cp ->
                addView(TextView(ctx).apply {
                    text = cp.toString()
                    typeface = Brand.mono(ctx, 700)
                    setTextColor(Brand.textPrimary)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                })
                addView(TextView(ctx).apply {
                    text = " CP"
                    typeface = Brand.display(ctx, 600)
                    setTextColor(Brand.textTertiary)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
                })
            }
        }
        val texts = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            addView(nameRow)
            if (v.cp != null) addView(cpLine)
        }
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(logo)
            addView(texts)
        }
    }

    private fun hundoBadge(): View {
        val ctx = this
        return TextView(this).apply {
            text = "HUNDO"
            typeface = Brand.display(ctx, 700)
            setTextColor(Brand.textOnGold)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 9f)
            letterSpacing = 0.1f
            val ph = Brand.dp(ctx, 6)
            val pvv = Brand.dp(ctx, 2)
            setPadding(ph, pvv, ph, pvv)
            background = Brand.cardBackground(ctx, radiusDp = 999f, fill = Brand.gold, stroke = Brand.gold)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply { leftMargin = Brand.dp(ctx, 8) }
        }
    }

    /** IV% + each league's rank, tier-coloured, laid out as evenly-weighted mini stats. */
    private fun statRow(v: Verdict): View {
        val ctx = this
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.TOP
        }
        row.addView(miniStat("IV", "${Math.round(v.ivPercent)}%", Brand.tierColor(v.ivPercent), null))
        for (lg in v.leagues) {
            row.addView(statDivider())
            row.addView(miniStat(
                lg.label,
                "#${lg.rank}",
                Brand.tierColor(lg.percent),
                String.format("%.1f%% · %d", lg.percent, lg.total),
            ))
        }
        return row
    }

    private fun miniStat(label: String, value: String, valueColor: Int, sub: String?): View {
        val ctx = this
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            addView(TextView(ctx).apply {
                text = label
                typeface = Brand.display(ctx, 600)
                setTextColor(Brand.textTertiary)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 9f)
                letterSpacing = 0.1f
            })
            addView(TextView(ctx).apply {
                text = value
                typeface = Brand.mono(ctx, 700)
                setTextColor(valueColor)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                setPadding(0, Brand.dp(ctx, 3), 0, 0)
            })
            if (sub != null) addView(TextView(ctx).apply {
                text = sub
                typeface = Brand.mono(ctx, 500)
                setTextColor(Brand.textTertiary)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
                setPadding(0, Brand.dp(ctx, 2), 0, 0)
            })
        }
    }

    private fun statDivider(): View = View(this).apply {
        layoutParams = LinearLayout.LayoutParams(Brand.dp(this@OverlayService, 1), Brand.dp(this@OverlayService, 30)).apply {
            leftMargin = Brand.dp(this@OverlayService, 8)
            rightMargin = Brand.dp(this@OverlayService, 8)
        }
        setBackgroundColor(Brand.borderSubtle)
    }

    private fun movesLine(lg: LeagueStanding): View {
        val ctx = this
        return TextView(this).apply {
            text = "${lg.label} · ${lg.moves}"
            typeface = Brand.body(ctx, 500)
            setTextColor(Brand.textSecondary)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
            setPadding(0, Brand.dp(ctx, 10), 0, 0)
        }
    }

    private fun footerRow(): View {
        val ctx = this
        return TextView(this).apply {
            text = "On-device · nothing left your phone · tap to dismiss"
            typeface = Brand.body(ctx, 400)
            setTextColor(Brand.textTertiary)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        }
    }

    private fun divider(topDp: Int, bottomDp: Int): View = View(this).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, Brand.dp(this@OverlayService, 1),
        ).apply {
            topMargin = Brand.dp(this@OverlayService, topDp)
            bottomMargin = Brand.dp(this@OverlayService, bottomDp)
        }
        setBackgroundColor(Brand.borderSubtle)
    }

    /** Position a finished card near the ball (below it, or above if there's no room) and show it. */
    private fun attachCard(container: View, estHeightDp: Int, blur: Boolean) {
        val params = overlayParams(
            Brand.dp(this, 300),
            WindowManager.LayoutParams.WRAP_CONTENT,
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            // Backdrop blur behind the card so it floats legibly over arbitrary game art
            // (design system: scrim + blur). Localised to the card's own bounds.
            if (blur && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                flags = flags or WindowManager.LayoutParams.FLAG_BLUR_BEHIND
                setBlurBehindRadius(Brand.dp(this@OverlayService, 24))
            }
            val dm = resources.displayMetrics
            val ballSize = (dm.density * 56).toInt()
            val margin = (dm.density * 8).toInt()
            val cardW = Brand.dp(this@OverlayService, 300)
            val estCardH = Brand.dp(this@OverlayService, estHeightDp)
            val bx = ballParams?.x ?: margin
            val by = ballParams?.y ?: 300
            x = bx.coerceIn(margin, (dm.widthPixels - cardW - margin).coerceAtLeast(margin))
            // Below the ball, or above it if there isn't room — never on top of it.
            val belowY = by + ballSize + margin
            y = if (belowY + estCardH <= dm.heightPixels) belowY
            else (by - estCardH - margin).coerceAtLeast(margin)
        }
        removeCard()
        runCatching { windowManager.addView(container, params) }
        card = container
    }

    private fun removeCard() {
        card?.let { runCatching { windowManager.removeView(it) } }
        card = null
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
        var longFired = false
        val slop = resources.displayMetrics.density * 12
        // Debug-only: long-press renders a sample verdict so the HUD card can be reviewed
        // without a live game scan. Never armed in release builds.
        val longPress = Runnable {
            if (!dragging) { longFired = true; showVerdictCard(sampleVerdict()) }
        }
        view.setOnTouchListener { v, e ->
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    downX = e.rawX; downY = e.rawY
                    startX = params.x; startY = params.y
                    dragging = false
                    longFired = false
                    if (isDebuggable) main.postDelayed(longPress, 600)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = e.rawX - downX
                    val dy = e.rawY - downY
                    if (!dragging && hypot(dx, dy) > slop) {
                        dragging = true
                        main.removeCallbacks(longPress)
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
                    main.removeCallbacks(longPress)
                    if (dragging) {
                        val close = inCloseZone(e.rawY)
                        hideCloseTarget()
                        if (close) quit() else snapToEdge(v, params)
                    } else if (!longFired) {
                        onBallTapped()
                    }
                    true
                }
                else -> false
            }
        }
    }

    /** True when this is a debuggable build — gates dev-only affordances. */
    private val isDebuggable: Boolean by lazy {
        (applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    /** A representative verdict for visual review of the HUD card (debug long-press). */
    private fun sampleVerdict(): Verdict = Verdict(
        name = "Specimen-12",
        cp = 1498,
        ivAtk = 15, ivDef = 15, ivSta = 14,
        recognized = true,
        leagues = listOf(
            LeagueStanding("GREAT", "Great League", 12, 4096, 99.2, 1498, "Quick Jab · Surge Beam / Iron Crash"),
            LeagueStanding("ULTRA", "Ultra League", 188, 4096, 95.4, 2487, "Quick Jab · Surge Beam / Iron Crash"),
        ),
    )

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
            typeface = Brand.display(this@OverlayService, 700)
            setTextColor(Brand.textPrimary)
            textSize = 24f
            gravity = Gravity.CENTER
            alpha = 0.85f
            background = Brand.cardBackground(
                this@OverlayService, radiusDp = 999f,
                fill = Brand.redBright, stroke = Brand.borderStrong, strokeDp = 1.5f,
            )
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

    private fun startForegroundCompat() {
        val mgr = getSystemService(NotificationManager::class.java)
        mgr.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, "PvP Overlay", NotificationManager.IMPORTANCE_LOW)
        )
        val notif: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("PvPeek")
            .setContentText("Tap the PvPeek button over a Pokémon to analyse it")
            .setSmallIcon(R.drawable.ic_mark)
            .setOngoing(true)
            .build()
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                startForeground(NOTIF_ID, notif, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION)
            else
                startForeground(NOTIF_ID, notif)
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
