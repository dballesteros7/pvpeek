package com.pogopvp.overlay

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.os.Handler
import android.os.HandlerThread
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Captures a single CURRENT frame on demand.
 *
 * IMPORTANT: Android 14+ forbids creating more than one VirtualDisplay from the same
 * MediaProjection (and forbids re-deriving a projection from the saved consent token).
 * So we create exactly ONE virtual display for the whole session and keep it alive.
 *
 * Battery is still preserved: we never consume frames while idle, so after a couple of
 * frames the mirror's buffer queue fills and back-pressures the compositor to a stop — no
 * ongoing work. On a scan we drain the stale buffer, which lets the compositor produce one
 * fresh frame of the *current* screen, and we grab that. One frame composited per tap.
 */
class ScreenCapture(
    private val context: Context,
    private val projection: MediaProjection
) {
    private val thread = HandlerThread("screen-capture").apply { start() }
    private val handler = Handler(thread.looper)

    private var reader: ImageReader? = null
    private var display: VirtualDisplay? = null
    private var width = 0
    private var height = 0

    /** Set while a capture is awaiting its fresh frame; consumed by the image listener. */
    private var pending: ((Bitmap?) -> Unit)? = null

    init {
        // Create the single VirtualDisplay promptly so the projection token isn't left
        // unused long enough to time out (another way modern Android rejects it).
        handler.post { runCatching { ensureDisplay() } }
    }

    fun captureFrame(onResult: (Bitmap?) -> Unit) {
        handler.post {
            val r = try {
                ensureDisplay()
            } catch (t: Throwable) {
                onResult(null)
                return@post
            }
            // Drop any stale buffered frame so the mirror re-composites the current screen.
            r.acquireLatestImage()?.close()
            pending = onResult
            // Safety net: if no fresh frame arrives, deliver whatever's latest (or null).
            handler.postDelayed({
                val cb = pending ?: return@postDelayed
                pending = null
                deliver(cb, r.acquireLatestImage())
            }, FRAME_TIMEOUT_MS)
        }
    }

    private fun ensureDisplay(): ImageReader {
        reader?.let { return it }
        val (w, h, density) = screenSize()
        width = w
        height = h
        val r = ImageReader.newInstance(w, h, PixelFormat.RGBA_8888, 2)
        r.setOnImageAvailableListener({ ir ->
            // Idle (no capture requested): leave the frame queued so the mirror stalls.
            val cb = pending ?: return@setOnImageAvailableListener
            pending = null
            deliver(cb, ir.acquireLatestImage())
        }, handler)
        display = projection.createVirtualDisplay(
            "pvp-scan",
            w, h, density,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            r.surface,
            null,
            handler
        )
        reader = r
        return r
    }

    private fun deliver(cb: (Bitmap?) -> Unit, image: Image?) {
        if (image == null) {
            cb(null)
            return
        }
        val bitmap = runCatching { image.toBitmap(width, height) }.getOrNull()
        image.close()
        cb(bitmap)
    }

    fun release() {
        handler.post {
            runCatching { display?.release() }
            runCatching { reader?.close() }
            display = null
            reader = null
        }
        thread.quitSafely()
    }

    private data class Size(val width: Int, val height: Int, val density: Int)

    private fun screenSize(): Size {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        wm.defaultDisplay.getRealMetrics(metrics)
        return Size(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi)
    }

    /** RGBA_8888 buffers carry row padding; crop it off so the bitmap isn't skewed. */
    private fun Image.toBitmap(width: Int, height: Int): Bitmap {
        val plane = planes[0]
        val pixelStride = plane.pixelStride
        val rowStride = plane.rowStride
        val rowPadding = rowStride - pixelStride * width
        val padded = Bitmap.createBitmap(
            width + rowPadding / pixelStride,
            height,
            Bitmap.Config.ARGB_8888
        )
        padded.copyPixelsFromBuffer(plane.buffer)
        return if (rowPadding == 0) padded
        else Bitmap.createBitmap(padded, 0, 0, width, height).also { padded.recycle() }
    }

    companion object {
        private const val FRAME_TIMEOUT_MS = 800L
    }
}
