package com.pogopvp.overlay

/**
 * Where the three appraisal IV bars sit on screen, expressed as **fractions** of the
 * frame so the same numbers work regardless of the bitmap's pixel resolution.
 *
 * Each bar is described by the horizontal extent of its filled track
 * ([barLeftFrac]..[barRightFrac]) and the vertical centre of the track
 * ([barCenterYFrac]). AppraisalScanner samples a few rows around that centre Y, walks
 * left→right between the two X fractions, and measures how far the accent colour reaches.
 *
 * ─────────────────────────────────────────────────────────────────────────────────────
 * CALIBRATION (one-time, per device profile):
 *
 *   The Flip 7 profile is CALIBRATED from a real screenshot; the Flip 5 profile's Y values
 *   are still estimated and need one real Flip 5 screenshot to confirm. Calibrating means
 *   editing ONLY the fraction constants below — no other code has to change.
 *
 *   How to calibrate:
 *     1. Take a screenshot of the appraisal screen (the page with the three IV bars).
 *     2. Open it in any image viewer that shows pixel coordinates.
 *     3. For each bar, read the pixel X of the left edge of the *track* and the right
 *        edge of the *track* (the full bar, NOT just the filled part), and the pixel Y
 *        at the vertical middle of that bar.
 *     4. Divide X by image width and Y by image height to get the fractions.
 *     5. Paste them into the matching bar below.
 *     6. Optionally use AppraisalScanner.calibrationDump(...) to print what the reader
 *        currently detects and confirm the regions line up.
 * ─────────────────────────────────────────────────────────────────────────────────────
 */
data class DeviceProfile(
    /** Human label, handy in [AppraisalScanner.calibrationDump]. */
    val name: String,
    val atk: BarRegion,
    val def: BarRegion,
    val sta: BarRegion,
    /** OCR boxes — the appraisal screen also shows CP (top), the species name (card),
     *  the type text (below the name) for regional-variant disambiguation, and the
     *  caught-info line at the bottom ("This X was caught…") which carries the REAL name
     *  even when the card shows a nickname. */
    val cpBox: TextRegion,
    val nameBox: TextRegion,
    val typeBox: TextRegion,
    val caughtBox: TextRegion
)

/** One IV bar's track, as fractions of the frame's width (X) and height (Y). */
data class BarRegion(
    val barLeftFrac: Double,
    val barRightFrac: Double,
    val barCenterYFrac: Double
)

/** A rectangular OCR region, as fractions of the frame's width and height. */
data class TextRegion(
    val leftFrac: Double,
    val topFrac: Double,
    val rightFrac: Double,
    val bottomFrac: Double
)

object DeviceProfiles {

    /**
     * Galaxy Z Flip 7 inner display: 1080×2520, portrait. **CALIBRATED** from a real
     * appraisal screenshot (Greninja, 2026-06-15) — these are measured, not guessed.
     *
     * Key finding: the appraisal screen shows the trainer character on the RIGHT half, so
     * the IV bars occupy only the LEFT ~half of the width (track ≈ 11.8%..46.4%), not the
     * full width. The three bars sit low on the screen at a constant ~4.1%-of-height pitch.
     *
     * Derived from a 685×1600 capture (same aspect as 1080×2520): Defense bar (full = IV15)
     * spanned x 81..318; bar centres at y 1258 / 1324 / 1390.
     */
    val flip7Inner = DeviceProfile(
        name = "Galaxy Z Flip 7 inner (1080x2520)",
        atk = BarRegion(barLeftFrac = 0.118, barRightFrac = 0.464, barCenterYFrac = 0.786),
        def = BarRegion(barLeftFrac = 0.118, barRightFrac = 0.464, barCenterYFrac = 0.828),
        sta = BarRegion(barLeftFrac = 0.118, barRightFrac = 0.464, barCenterYFrac = 0.869),
        // CP banner near the top; species name centred on the card (wide box for long names).
        cpBox = TextRegion(leftFrac = 0.27, topFrac = 0.048, rightFrac = 0.73, bottomFrac = 0.088),
        nameBox = TextRegion(leftFrac = 0.10, topFrac = 0.36, rightFrac = 0.90, bottomFrac = 0.43),
        // Type text sits below the name (~y0.53). Generous band tolerates the vertical drift
        // from nickname tags; left-of-centre to avoid the trainer occluding the 2nd type.
        typeBox = TextRegion(leftFrac = 0.18, topFrac = 0.50, rightFrac = 0.66, bottomFrac = 0.585),
        // Bottom caught-info banner. Generous band: it grows UPWARD as the location/flavor
        // wraps to more lines (special catches like "Antique Form Sinistea?! Awesome catch!
        // This Sinistea was caught…"), so "This X was caught" drifts up — top kept low enough
        // to keep that line in view; the regex finds it anywhere inside.
        caughtBox = TextRegion(leftFrac = 0.04, topFrac = 0.855, rightFrac = 0.96, bottomFrac = 0.975)
    )

    /**
     * Galaxy Z Flip 5 inner display: 1080×2640, portrait. The width matches the Flip 7, so
     * the horizontal track fractions are shared. The Y fractions here are ESTIMATED by
     * bottom-anchoring the Flip 7 bars onto the taller 2640 panel and still need a one real
     * Flip 5 appraisal screenshot to confirm — see the class-level calibration note.
     */
    val flip5Inner = DeviceProfile(
        name = "Galaxy Z Flip 5 inner (1080x2640) — Y estimated, needs screenshot",
        atk = BarRegion(barLeftFrac = 0.118, barRightFrac = 0.464, barCenterYFrac = 0.796),
        def = BarRegion(barLeftFrac = 0.118, barRightFrac = 0.464, barCenterYFrac = 0.836),
        sta = BarRegion(barLeftFrac = 0.118, barRightFrac = 0.464, barCenterYFrac = 0.875),
        // Estimated from the Flip 7; refine when a real Flip 5 screenshot arrives.
        cpBox = TextRegion(leftFrac = 0.27, topFrac = 0.045, rightFrac = 0.73, bottomFrac = 0.083),
        nameBox = TextRegion(leftFrac = 0.10, topFrac = 0.345, rightFrac = 0.90, bottomFrac = 0.41),
        typeBox = TextRegion(leftFrac = 0.18, topFrac = 0.48, rightFrac = 0.66, bottomFrac = 0.565),
        caughtBox = TextRegion(leftFrac = 0.04, topFrac = 0.855, rightFrac = 0.96, bottomFrac = 0.975)
    )

    /**
     * Pick the profile matching the captured frame. Flip 5 and Flip 7 share width (1080)
     * but differ in height (2640 vs 2520); we key on height. Anything else falls back to
     * the calibrated Flip 7 profile as the sane default.
     */
    fun forBitmap(width: Int, height: Int): DeviceProfile {
        return if (height >= 2600) flip5Inner else flip7Inner
    }
}
