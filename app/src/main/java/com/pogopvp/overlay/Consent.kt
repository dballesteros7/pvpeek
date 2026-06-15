package com.pogopvp.overlay

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * User consent for diagnostics, persisted in SharedPreferences.
 *
 * - Analytics: OFF until the user opts in (consent, GDPR Art. 6(1)(a) / ePrivacy). [analyticsAsked]
 *   distinguishes "not yet asked" from an explicit "no".
 * - Crash reporting: ON by default (legitimate interest), with an opt-out.
 *
 * [apply] pushes the current choices into the Firebase SDKs; call it on every app/service start.
 */
object Consent {

    private const val PREFS = "pvpeek_consent"
    private const val KEY_ANALYTICS = "analytics" // absent = never asked
    private const val KEY_CRASH = "crash"

    private fun prefs(c: Context) = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun analyticsAsked(c: Context): Boolean = prefs(c).contains(KEY_ANALYTICS)
    fun analyticsGranted(c: Context): Boolean = prefs(c).getBoolean(KEY_ANALYTICS, false)
    fun crashEnabled(c: Context): Boolean = prefs(c).getBoolean(KEY_CRASH, true)

    fun setAnalytics(c: Context, granted: Boolean) {
        prefs(c).edit().putBoolean(KEY_ANALYTICS, granted).apply()
        apply(c)
    }

    fun setCrash(c: Context, enabled: Boolean) {
        prefs(c).edit().putBoolean(KEY_CRASH, enabled).apply()
        apply(c)
    }

    /** Reflect the stored choices in the Firebase SDKs. */
    fun apply(c: Context) {
        runCatching {
            FirebaseAnalytics.getInstance(c).setAnalyticsCollectionEnabled(analyticsGranted(c))
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(crashEnabled(c))
        }
    }
}
