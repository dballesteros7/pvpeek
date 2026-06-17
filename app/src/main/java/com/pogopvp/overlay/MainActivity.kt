package com.pogopvp.overlay

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * One-time launcher: secures the "draw over other apps" permission and screen-capture consent,
 * then starts the persistent PvPeek button ([OverlayService]) and gets out of the way. Consent is
 * requested here at launch, so the service runs as a `mediaProjection` foreground service.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var projectionManager: MediaProjectionManager

    private val notifLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* best-effort: the FGS still runs, just without a visible notification */ }

    private val projectionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            OverlayService.start(this, result.resultCode, data)
            Toast.makeText(
                this,
                "PvPeek is ready — open Pokémon GO and tap the button on an Appraisal screen",
                Toast.LENGTH_LONG
            ).show()
            moveTaskToBack(true)
        } else {
            Toast.makeText(this, "Screen-capture permission is needed to scan", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectionManager = getSystemService(MediaProjectionManager::class.java)

        fun pad(v: Int) = Brand.dp(this, v)

        // Brand mark — the crown-swords logo (same asset as the launcher icon).
        val logo = ImageView(this).apply {
            setImageResource(R.drawable.ic_icon)
            val s = pad(76)
            layoutParams = LinearLayout.LayoutParams(s, s)
        }
        val title = Brand.heading(this, "PvPeek", 34f).apply {
            gravity = Gravity.CENTER
        }
        val tagline = TextView(this).apply {
            text = "Glance · Verdict · Done."
            typeface = Brand.display(this@MainActivity, 600)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            setTextColor(Brand.gold)
            letterSpacing = 0.06f
            gravity = Gravity.CENTER
        }
        val privacy = Brand.bodyText(
            this,
            "A floating PvPeek button sits on the edge of your screen. Tap it on a " +
                "creature's Appraisal to read its IVs and PvP rank in under two seconds.\n\n" +
                "Your screen is never recorded or saved. When you tap Start PvPeek, Android " +
                "asks for screen-capture permission; after that each tap reads a single frame — " +
                "nothing is captured between taps and nothing leaves your phone.\n\n" +
                "Drag the button to move it. Drag it to the bottom of the screen to close.",
        ).apply { gravity = Gravity.START }
        val privacyPromise = Brand.overline(this, "On-device · Nothing leaves your phone", Brand.textSecondary).apply {
            gravity = Gravity.CENTER
        }
        val startButton = Brand.button(this, "Start PvPeek", primary = true) { launchFlow() }
        val legalButton = Brand.button(this, "About & Legal", primary = false) {
            startActivity(Intent(this@MainActivity, LegalActivity::class.java))
        }

        fun LinearLayout.addSpaced(view: android.view.View, topDp: Int, fullWidth: Boolean = false) {
            val lp = LinearLayout.LayoutParams(
                if (fullWidth) LinearLayout.LayoutParams.MATCH_PARENT
                else LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply { topMargin = pad(topDp) }
            addView(view, lp)
        }

        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setBackgroundColor(Brand.bgApp)
            setPadding(pad(28), pad(56), pad(28), pad(48))
            addSpaced(logo, 0)
            addSpaced(title, 20)
            addSpaced(tagline, 8)
            addSpaced(privacy, 28, fullWidth = true)
            addSpaced(privacyPromise, 24, fullWidth = true)
            addSpaced(startButton, 28, fullWidth = true)
            addSpaced(legalButton, 12, fullWidth = true)
        }
        setContentView(ScrollView(this).apply {
            setBackgroundColor(Brand.bgApp)
            addView(column)
        })

        Consent.apply(this)
        maybeAskAnalyticsConsent()
    }

    /** First-run opt-in for anonymous analytics (off until the user agrees). */
    private fun maybeAskAnalyticsConsent() {
        if (Consent.analyticsAsked(this)) return
        AlertDialog.Builder(this)
            .setTitle("Help improve PvPeek?")
            .setMessage(
                "Share usage stats that don't identify you by name — which Pokémon you analyse and " +
                "whether the read succeeded — so we can fix reading errors. No screen content or " +
                "images are sent. You can change this anytime in About & Legal."
            )
            .setPositiveButton("Allow") { _, _ -> Consent.setAnalytics(this, true) }
            .setNegativeButton("No thanks") { _, _ -> Consent.setAnalytics(this, false) }
            .setNeutralButton("Privacy policy") { _, _ ->
                runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(LegalActivity.PRIVACY_URL))) }
            }
            .setCancelable(false)
            .show()
    }

    private fun launchFlow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (!Settings.canDrawOverlays(this)) {
            startActivity(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
            )
            Toast.makeText(
                this,
                "Grant \"Draw over other apps\", then tap the button again",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        // Ask for screen-capture consent now; the result handler starts the overlay service.
        projectionLauncher.launch(projectionManager.createScreenCaptureIntent())
    }
}
