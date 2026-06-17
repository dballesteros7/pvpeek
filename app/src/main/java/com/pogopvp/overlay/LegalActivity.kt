package com.pogopvp.overlay

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/** About / Legal: version, affiliation disclaimer, privacy policy link, and open-source notices. */
class LegalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fun pad(v: Int) = Brand.dp(this, v)

        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Brand.bgApp)
            setPadding(pad(24), pad(40), pad(24), pad(40))
        }

        fun add(view: View, topDp: Int) {
            column.addView(view, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply { topMargin = pad(topDp) })
        }

        // Overlines act as section headings in the display font.
        fun section(label: String) = add(Brand.overline(this, label), 28)
        fun body(text: String) = Brand.bodyText(this, text, sizeSp = 14f)

        fun switchRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) =
            Switch(this).apply {
                text = label
                typeface = Brand.body(this@LegalActivity, 500)
                setTextColor(Brand.textSecondary)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                isChecked = checked
                setPadding(0, pad(8), 0, pad(8))
                setOnCheckedChangeListener { _, value -> onChange(value) }
            }

        add(Brand.heading(this, "PvPeek", 26f), 0)
        add(TextView(this).apply {
            text = versionString()
            typeface = Brand.mono(this@LegalActivity, 500)
            setTextColor(Brand.textTertiary)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        }, 6)

        section("Disclaimer")
        add(body(
            "PvPeek is an independent, unofficial tool. It is not affiliated with, endorsed " +
            "by, or sponsored by Niantic, Nintendo, Game Freak, or The Pokémon Company. " +
            "\"Pokémon\" and \"Pokémon GO\" are trademarks of their respective owners."
        ), 10)

        section("Privacy")
        add(body(
            "What's on your screen never leaves your phone. PvPeek reads a single frame only " +
            "when you tap, analyses it on-device, and never stores or sends the image or your " +
            "screen's text. It sends crash reports, and — only if you opt in below — usage " +
            "analytics that don't identify you by name (including the recognised species name)."
        ), 10)
        add(Brand.button(this, "View privacy policy", primary = false) { openUrl(PRIVACY_URL) }, 16)

        section("Your data choices")
        add(switchRow(
            "Share usage analytics (no name, no screen content)",
            Consent.analyticsGranted(this),
        ) { Consent.setAnalytics(this, it) }, 8)
        add(switchRow(
            "Send crash reports",
            Consent.crashEnabled(this),
        ) { Consent.setCrash(this, it) }, 4)

        section("Open-source licenses")
        add(body(LICENSES), 10)
        add(Brand.button(this, "Source on GitHub", primary = false) { openUrl(REPO_URL) }, 16)

        setContentView(ScrollView(this).apply {
            setBackgroundColor(Brand.bgApp)
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
            PvPeek — MIT License, Copyright (c) 2026 Diego Alfonso Ballesteros Villamizar.

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
