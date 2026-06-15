package com.pogopvp.overlay

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Invisible, throwaway activity. MediaProjection consent can only be requested from an
 * Activity, so the PvPeek button launches this on its first tap: it immediately shows the system
 * screen-capture dialog, forwards the result token to [OverlayService], and finishes.
 */
class ProjectionRequestActivity : ComponentActivity() {

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            OverlayService.deliverProjection(this, result.resultCode, data)
        } else {
            OverlayService.projectionDenied(this)
        }
        // Remove our isolated task entirely so the system returns to the previous app
        // (Pokémon GO) rather than showing our launcher.
        finishAndRemoveTask()
        overridePendingTransition(0, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mpm = getSystemService(MediaProjectionManager::class.java)
        launcher.launch(mpm.createScreenCaptureIntent())
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(
                Intent(context, ProjectionRequestActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        }
    }
}
