package com.pogopvp.overlay

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/** About / Legal: version, affiliation disclaimer, privacy policy link, and open-source notices. */
class LegalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#15161A"))
            setPadding(48, 80, 48, 80)
        }

        fun heading(text: String) = TextView(this).apply {
            this.text = text
            textSize = 18f
            setTextColor(Color.WHITE)
            setPadding(0, 36, 0, 10)
        }

        fun body(text: String) = TextView(this).apply {
            this.text = text
            textSize = 14f
            setTextColor(Color.parseColor("#CFCFD4"))
            setLineSpacing(0f, 1.15f)
        }

        column.addView(TextView(this).apply {
            text = "PvPeek"
            textSize = 24f
            setTextColor(Color.WHITE)
        })
        column.addView(body(versionString()))

        column.addView(heading("Disclaimer"))
        column.addView(body(
            "PvPeek is an independent, unofficial tool. It is not affiliated with, endorsed " +
            "by, or sponsored by Niantic, Nintendo, Game Freak, or The Pokémon Company. " +
            "\"Pokémon\" and \"Pokémon GO\" are trademarks of their respective owners."
        ))

        column.addView(heading("Privacy"))
        column.addView(body(
            "What's on your screen never leaves your phone. PvPeek reads a single frame only " +
            "when you tap, analyses it on-device, and never stores or sends the image or your " +
            "screen's text. It sends anonymous crash reports, and — only if you opt in below — " +
            "anonymous usage analytics (including the recognised species name)."
        ))
        column.addView(Button(this).apply {
            text = "View privacy policy"
            setOnClickListener { openUrl(PRIVACY_URL) }
        })

        column.addView(heading("Your data choices"))
        column.addView(Switch(this).apply {
            text = "Share anonymous usage analytics"
            setTextColor(Color.parseColor("#CFCFD4"))
            isChecked = Consent.analyticsGranted(this@LegalActivity)
            setOnCheckedChangeListener { _, checked -> Consent.setAnalytics(this@LegalActivity, checked) }
        })
        column.addView(Switch(this).apply {
            text = "Send crash reports"
            setTextColor(Color.parseColor("#CFCFD4"))
            isChecked = Consent.crashEnabled(this@LegalActivity)
            setOnCheckedChangeListener { _, checked -> Consent.setCrash(this@LegalActivity, checked) }
        })

        column.addView(heading("Open-source licenses"))
        column.addView(body(LICENSES))
        column.addView(Button(this).apply {
            text = "Source on GitHub"
            setOnClickListener { openUrl(REPO_URL) }
        })

        setContentView(ScrollView(this).apply {
            setBackgroundColor(Color.parseColor("#15161A"))
            addView(column, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { gravity = Gravity.CENTER_HORIZONTAL })
        })
    }

    private fun versionString(): String = runCatching {
        val pi = packageManager.getPackageInfo(packageName, 0)
        val code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) pi.longVersionCode
        else @Suppress("DEPRECATION") pi.versionCode.toLong()
        "Version ${pi.versionName} ($code)"
    }.getOrDefault("")

    private fun openUrl(url: String) {
        runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
    }

    companion object {
        const val PRIVACY_URL = "https://dballesteros7.github.io/pvpeek/privacy.html"
        const val REPO_URL = "https://github.com/dballesteros7/pvpeek"

        private val LICENSES = """
            PvPeek — MIT License, Copyright (c) 2026 Diego Ballesteros.

            Game data, types, move data, and PvP rankings are derived from PvPoke
            (pvpoke.com), used under the MIT License, Copyright (c) 2019 pvpoke:

            Permission is hereby granted, free of charge, to any person obtaining a copy of
            this software and associated documentation files (the "Software"), to deal in the
            Software without restriction, including without limitation the rights to use, copy,
            modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
            and to permit persons to whom the Software is furnished to do so, subject to the
            following conditions: The above copyright notice and this permission notice shall be
            included in all copies or substantial portions of the Software. THE SOFTWARE IS
            PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.

            Also uses: Google ML Kit (text recognition), Firebase SDKs (Crashlytics, Analytics),
            and AndroidX libraries.
        """.trimIndent()
    }
}
