package com.pogopvp.overlay

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Lightweight, privacy-safe usage monitoring via Firebase Analytics.
 *
 * Logs ONLY derived signals about how a scan went — booleans, counts, the species name, and
 * a device bucket. It deliberately NEVER sends screen content, OCR text, the caught-info line
 * (which contains the user's location and catch date), or any image. This keeps the app's
 * "nothing from your screen leaves the phone" promise intact.
 */
object Analytics {

    /**
     * One event per ANALYZE tap. [outcome] = ok | name_unrecognized | capture_failed;
     * [nameSource] = caught | card | none; [frame] = a coarse device bucket.
     */
    fun logAppraise(
        context: Context,
        outcome: String,
        nameSource: String,
        typesDetected: Int,
        cpRead: Boolean,
        frame: String,
        species: String?
    ) {
        if (!Consent.analyticsGranted(context)) return // opt-in only
        runCatching {
            val params = Bundle().apply {
                putString("outcome", outcome)
                putString("name_source", nameSource)
                putLong("types_detected", typesDetected.toLong())
                putString("cp_read", cpRead.toString())
                putString("frame", frame)
                species?.take(60)?.let { putString("species", it) }
            }
            FirebaseAnalytics.getInstance(context).logEvent("appraise_result", params)
        }
    }
}
