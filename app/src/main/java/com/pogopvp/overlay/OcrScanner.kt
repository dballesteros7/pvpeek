package com.pogopvp.overlay

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.atomic.AtomicInteger

/**
 * On-device OCR for the CP and species name. Both are read from the appraisal screen (the
 * same frame the IV bars come from), cropped to the per-device [TextRegion]s so we OCR two
 * small rectangles instead of the whole frame.
 */
class OcrScanner {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun scan(frame: Bitmap, profile: DeviceProfile, onResult: (ScanResult) -> Unit) {
        val cpCrop = frame.crop(profile.cpBox)
        val nameCrop = frame.crop(profile.nameBox)
        val typeCrop = frame.crop(profile.typeBox)
        val caughtCrop = frame.crop(profile.caughtBox)
        val crops = listOf(cpCrop, nameCrop, typeCrop, caughtCrop)

        val texts = arrayOfNulls<String>(4)
        val remaining = AtomicInteger(4)

        fun complete(index: Int, text: String) {
            texts[index] = text
            if (remaining.decrementAndGet() == 0) {
                crops.forEach { it.recycle() }
                onResult(
                    ScanParser.parse(
                        cpText = texts[0].orEmpty(),
                        nameText = texts[1].orEmpty(),
                        typeText = texts[2].orEmpty(),
                        caughtText = texts[3].orEmpty()
                    )
                )
            }
        }

        recognize(cpCrop) { complete(0, it) }
        recognize(nameCrop) { complete(1, it) }
        recognize(typeCrop) { complete(2, it) }
        recognize(caughtCrop) { complete(3, it) }
    }

    private fun recognize(bitmap: Bitmap, onText: (String) -> Unit) {
        recognizer.process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener { onText(it.text) }
            .addOnFailureListener { onText("") }
    }

    fun close() = recognizer.close()

    private fun Bitmap.crop(r: TextRegion): Bitmap {
        val x = (r.leftFrac * width).toInt().coerceIn(0, width - 1)
        val y = (r.topFrac * height).toInt().coerceIn(0, height - 1)
        val w = ((r.rightFrac - r.leftFrac) * width).toInt().coerceIn(1, width - x)
        val h = ((r.bottomFrac - r.topFrac) * height).toInt().coerceIn(1, height - y)
        return Bitmap.createBitmap(this, x, y, w, h)
    }
}
