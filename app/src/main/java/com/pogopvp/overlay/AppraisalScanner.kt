package com.pogopvp.overlay

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

/** The three IVs read off the appraisal bars, plus the raw fill fractions for debugging. */
data class AppraisalResult(
    val ivAtk: Int,
    val ivDef: Int,
    val ivSta: Int,
    /** Fill fraction (0.0..1.0) of each bar before rounding, in (atk, def, sta) order. */
    val rawFractions: Triple<Double, Double, Double>
)

/**
 * Reads Pokémon GO's appraisal IV bars by PIXEL ANALYSIS — OCR cannot read a coloured
 * progress bar, so we measure how much of each bar's track is painted with the accent
 * colour over the empty track.
 *
 * The bars' HORIZONTAL track is stable (fixed fraction of width), but their VERTICAL
 * position shifts between Pokémon/screens — so we do NOT sample fixed Y lines. Instead we
 * scan a vertical window and DETECT the three bar bands: a bar row is one where almost the
 * whole track is "bar-coloured" (filled accent OR the light-grey empty track), as opposed
 * to white card, text, or artwork. We then pick the evenly-spaced triple of bands (Attack,
 * Defense, HP top→bottom) and measure each one's fill. If detection fails we fall back to
 * the profile's fixed Y positions.
 */
object AppraisalScanner {

    // Vertical window (fraction of height) to search for the bars — generous, since the
    // bars drift. Covers the lower-middle where the appraisal card sits on both flips.
    private const val SEARCH_TOP_FRAC = 0.66
    private const val SEARCH_BOTTOM_FRAC = 0.94

    // A row counts as part of a bar when this fraction of its track is bar-coloured.
    private const val COVER_THRESHOLD = 0.80
    private const val MIN_BAND_PX = 8          // ignore bands thinner than a real bar
    private const val MAX_INNER_GAP_PX = 2     // bridge tiny sub-threshold dips within a band

    // Plausible centre-to-centre spacing of adjacent bars, as a fraction of height.
    private const val MIN_PITCH_FRAC = 0.022
    private const val MAX_PITCH_FRAC = 0.075

    // Fill = bright AND saturated (the vivid accent). Empty track = light, low-saturation grey.
    private const val LUMA_THRESHOLD = 0.42
    private const val SAT_THRESHOLD = 0.28
    private const val TRACK_LUMA_MIN = 0.72
    private const val TRACK_LUMA_MAX = 0.94
    private const val TRACK_SAT_MAX = 0.12

    fun read(frame: Bitmap, profile: DeviceProfile): AppraisalResult {
        detectFractions(frame, profile)?.let { (fa, fd, fs) ->
            return AppraisalResult(toIv(fa), toIv(fd), toIv(fs), Triple(fa, fd, fs))
        }
        // Fallback: fixed-Y sampling from the profile (legacy behaviour).
        val fa = fixedFillFraction(frame, profile.atk)
        val fd = fixedFillFraction(frame, profile.def)
        val fs = fixedFillFraction(frame, profile.sta)
        return AppraisalResult(toIv(fa), toIv(fd), toIv(fs), Triple(fa, fd, fs))
    }

    private fun toIv(fraction: Double): Int = (fraction * 15.0).roundToInt().coerceIn(0, 15)

    // --- Dynamic bar detection --------------------------------------------

    /** (atk, def, sta) fill fractions from the three detected bands, or null if not found. */
    private fun detectFractions(frame: Bitmap, profile: DeviceProfile): Triple<Double, Double, Double>? {
        val w = frame.width
        val h = frame.height
        val xLeft = (profile.atk.barLeftFrac * w).toInt().coerceIn(0, w - 2)
        val xRight = (profile.atk.barRightFrac * w).toInt().coerceIn(xLeft + 1, w - 1)
        val regionW = xRight - xLeft + 1

        val yTop = (SEARCH_TOP_FRAC * h).toInt().coerceIn(0, h - 2)
        val yBot = (SEARCH_BOTTOM_FRAC * h).toInt().coerceIn(yTop + 1, h - 1)
        val regionH = yBot - yTop + 1

        val px = IntArray(regionW * regionH)
        frame.getPixels(px, 0, regionW, xLeft, yTop, regionW, regionH)

        // Per-row: is this a "bar row" (track mostly filled-or-empty-track)?
        val barRow = BooleanArray(regionH)
        for (ry in 0 until regionH) {
            var covered = 0
            val base = ry * regionW
            for (rx in 0 until regionW) {
                val p = px[base + rx]
                if (isFilled(p) || isTrackGrey(p)) covered++
            }
            barRow[ry] = covered.toDouble() / regionW >= COVER_THRESHOLD
        }

        // Group consecutive bar rows into bands (bridging tiny gaps).
        val bands = ArrayList<IntArray>() // each: [startRy, endRy]
        var ry = 0
        while (ry < regionH) {
            if (!barRow[ry]) { ry++; continue }
            var end = ry
            var gap = 0
            var scan = ry + 1
            while (scan < regionH && (barRow[scan] || gap < MAX_INNER_GAP_PX)) {
                if (barRow[scan]) { end = scan; gap = 0 } else gap++
                scan++
            }
            if (end - ry + 1 >= MIN_BAND_PX) bands.add(intArrayOf(ry, end))
            ry = end + 1
        }
        if (bands.size < 3) return null

        val triple = chooseTriple(bands, h) ?: return null
        fun frac(band: IntArray): Double {
            val mids = ArrayList<Double>()
            for (y in band[0]..band[1]) mids.add(rowFillFraction(px, regionW, y))
            return median(mids)
        }
        return Triple(frac(triple[0]), frac(triple[1]), frac(triple[2]))
    }

    /** Pick three consecutive bands whose two gaps are most equal and within plausible pitch. */
    private fun chooseTriple(bands: List<IntArray>, h: Int): List<IntArray>? {
        fun center(b: IntArray) = (b[0] + b[1]) / 2.0
        val minPitch = MIN_PITCH_FRAC * h
        val maxPitch = MAX_PITCH_FRAC * h
        var best: List<IntArray>? = null
        var bestScore = Double.MAX_VALUE
        for (i in 0..bands.size - 3) {
            val a = bands[i]; val b = bands[i + 1]; val c = bands[i + 2]
            val g1 = center(b) - center(a)
            val g2 = center(c) - center(b)
            if (g1 < minPitch || g1 > maxPitch || g2 < minPitch || g2 > maxPitch) continue
            val score = abs(g1 - g2)
            if (score < bestScore) { bestScore = score; best = listOf(a, b, c) }
        }
        return best
    }

    /** Rightmost filled X in a row of the region buffer → fraction of the track. */
    private fun rowFillFraction(px: IntArray, regionW: Int, ry: Int): Double {
        var lastFilled = -1
        val base = ry * regionW
        for (rx in 0 until regionW) {
            if (isFilled(px[base + rx])) lastFilled = rx
        }
        if (lastFilled < 0) return 0.0
        return (lastFilled.toDouble() / (regionW - 1)).coerceIn(0.0, 1.0)
    }

    // --- Fixed-Y fallback (used only if detection fails) -------------------

    private fun fixedFillFraction(frame: Bitmap, bar: BarRegion): Double {
        val w = frame.width
        val h = frame.height
        val xLeft = (bar.barLeftFrac * w).toInt().coerceIn(0, w - 1)
        val xRight = (bar.barRightFrac * w).toInt().coerceIn(xLeft + 1, w - 1)
        val span = xRight - xLeft
        val centerY = (bar.barCenterYFrac * h).toInt().coerceIn(0, h - 1)
        val fractions = ArrayList<Double>(7)
        for (i in 0 until 7) {
            val y = (centerY + (i - 3)).coerceIn(0, h - 1)
            var lastFilled = -1
            for (x in xLeft..xRight) if (isFilled(frame.getPixel(x, y))) lastFilled = x
            fractions.add(if (lastFilled < 0) 0.0 else ((lastFilled - xLeft).toDouble() / span))
        }
        return median(fractions)
    }

    // --- Pixel classifiers -------------------------------------------------

    /** Filled = bright AND saturated (the vivid accent fill). */
    private fun isFilled(pixel: Int): Boolean {
        val r = Color.red(pixel) / 255.0
        val g = Color.green(pixel) / 255.0
        val b = Color.blue(pixel) / 255.0
        val maxC = max(r, max(g, b))
        val minC = minOf(r, g, b)
        val luma = 0.299 * r + 0.587 * g + 0.114 * b
        val sat = if (maxC <= 0.0) 0.0 else (maxC - minC) / maxC
        return luma >= LUMA_THRESHOLD && sat >= SAT_THRESHOLD
    }

    /** Empty track = a light, near-neutral grey, distinctly dimmer than the white card. */
    private fun isTrackGrey(pixel: Int): Boolean {
        val r = Color.red(pixel) / 255.0
        val g = Color.green(pixel) / 255.0
        val b = Color.blue(pixel) / 255.0
        val maxC = max(r, max(g, b))
        val minC = minOf(r, g, b)
        val luma = 0.299 * r + 0.587 * g + 0.114 * b
        val sat = if (maxC <= 0.0) 0.0 else (maxC - minC) / maxC
        return luma in TRACK_LUMA_MIN..TRACK_LUMA_MAX && sat <= TRACK_SAT_MAX
    }

    private fun median(values: List<Double>): Double {
        if (values.isEmpty()) return 0.0
        val sorted = values.sorted()
        val mid = sorted.size / 2
        return if (sorted.size % 2 == 1) sorted[mid] else (sorted[mid - 1] + sorted[mid]) / 2.0
    }

    /** Diagnostic dump: the detected IVs/fractions, or a note that detection fell back. */
    fun calibrationDump(frame: Bitmap, profile: DeviceProfile): String {
        val r = read(frame, profile)
        return "AppraisalScanner — ${profile.name}\n" +
            "frame ${frame.width}x${frame.height}\n" +
            "ATK iv=${r.ivAtk} frac=${"%.3f".format(r.rawFractions.first)}\n" +
            "DEF iv=${r.ivDef} frac=${"%.3f".format(r.rawFractions.second)}\n" +
            "STA iv=${r.ivSta} frac=${"%.3f".format(r.rawFractions.third)}"
    }
}
