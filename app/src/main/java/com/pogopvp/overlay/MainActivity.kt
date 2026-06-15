package com.pogopvp.overlay

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * One-time launcher: secures the "draw over other apps" permission, then starts the
 * persistent Pokéball ([OverlayService]) and gets out of the way. Screen-capture consent is
 * NOT requested here — the Pokéball asks for it on its first tap.
 */
class MainActivity : AppCompatActivity() {

    private val notifLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* best-effort: the FGS still runs, just without a visible notification */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = TextView(this).apply {
            text = "PoGo PvP Overlay"
            textSize = 22f
            setTextColor(Color.WHITE)
            setPadding(0, 0, 0, 24)
        }
        val privacy = TextView(this).apply {
            text =
                "A floating Pokéball sits on the edge of your screen. Tap it on a Pokémon's " +
                "Appraisal to read its IVs and PvP rank.\n\n" +
                "Your screen is NOT recorded or saved. The first time you tap the Pokéball, " +
                "Android asks for screen-capture permission; after that each tap reads a " +
                "SINGLE frame — nothing is captured between taps and nothing leaves your phone.\n\n" +
                "Drag the Pokéball to move it. Drag it to the bottom of the screen to close."
            textSize = 15f
            setTextColor(Color.parseColor("#DDDDDD"))
            setLineSpacing(0f, 1.15f)
            setPadding(0, 0, 0, 36)
        }
        val button = Button(this).apply {
            text = "Show Pokéball"
            setOnClickListener { launchFlow() }
        }
        val legalButton = Button(this).apply {
            text = "About & Legal"
            setOnClickListener { startActivity(Intent(this@MainActivity, LegalActivity::class.java)) }
        }
        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setBackgroundColor(Color.parseColor("#15161A"))
            setPadding(48, 96, 48, 96)
            addView(title)
            addView(privacy)
            addView(button)
            addView(legalButton)
        }
        setContentView(ScrollView(this).apply {
            setBackgroundColor(Color.parseColor("#15161A"))
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
                "Share anonymous usage stats — which Pokémon you analyse and whether the read " +
                "succeeded — so we can fix reading errors. No screen content, images, or personal " +
                "data. You can change this anytime in About & Legal."
            )
            .setPositiveButton("Allow") { _, _ -> Consent.setAnalytics(this, true) }
            .setNegativeButton("No thanks") { _, _ -> Consent.setAnalytics(this, false) }
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
        OverlayService.start(this)
        Toast.makeText(
            this,
            "Pokéball ready — open Pokémon GO and tap it on an Appraisal screen",
            Toast.LENGTH_LONG
        ).show()
        moveTaskToBack(true)
    }
}
