package com.pogopvp.overlay

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

/**
 * PvPeek design system, ported to the app's programmatic (View-based) UI.
 *
 * This is the Kotlin counterpart of the skill's CSS tokens (`tokens/colors.css`,
 * `typography.css`, `spacing.css`). The UI is built in code rather than XML layouts,
 * so screens reference these tokens and the view/drawable builders below instead of
 * hand-coding hex values. Keep the colour values in sync with `res/values/colors.xml`.
 *
 * The brand is a dark esports-HUD: slate surfaces, gold primary accent, tabular mono
 * numerals leading the hierarchy, and the rank-tier colour language ([tierColor]).
 */
object Brand {

    // ---- Colours (mirror res/values/colors.xml) ----------------------------
    val bgApp = 0xFF15161A.toInt()
    val bgElevated = 0xFF1F222A.toInt()
    val surfaceCard = 0xFF262B36.toInt()
    val surfaceInset = 0xFF1A1C22.toInt()
    val surfaceRaised = 0xFF2E3440.toInt()
    val slate1000 = 0xFF0E0F12.toInt()

    val gold = 0xFFF4C430.toInt()
    val goldBright = 0xFFFCD116.toInt()
    val goldDeep = 0xFFC8920A.toInt()
    val blueAccent = 0xFF2E6BE6.toInt()
    val blueBright = 0xFF5B9DFF.toInt()
    val redBright = 0xFFFF4D5E.toInt()

    val textPrimary = 0xFFF7F9FC.toInt()
    val textSecondary = 0xFF9AA3B2.toInt()
    val textTertiary = 0xFF6B7280.toInt()
    val textOnGold = 0xFF1A1405.toInt()

    // Rank-tier scale — the core verdict colour language.
    val tierPerfect = 0xFFFCD116.toInt()
    val tierGreat = 0xFF38D996.toInt()
    val tierGood = 0xFF5B9DFF.toInt()
    val tierFair = 0xFF9AA3B2.toInt()
    val tierWeak = 0xFF6B7280.toInt()

    val moveFast = 0xFF5B9DFF.toInt()
    val moveCharged = 0xFFC77DFF.toInt()

    // Hairline borders over dark surfaces (white at low alpha).
    val borderSubtle = Color.argb(15, 255, 255, 255)   // ~0.06
    val borderDefault = Color.argb(26, 255, 255, 255)  // ~0.10
    val borderStrong = Color.argb(41, 255, 255, 255)   // ~0.16

    /**
     * Verdict-card backdrop. The card floats over arbitrary game art, so the scrim is
     * strong and near-opaque for legibility on devices without cross-window blur.
     */
    val scrimOverlay = Color.argb(235, 14, 15, 18)

    /**
     * Lighter scrim (`rgba(10,11,14,.82)`, the design system's value) used when the window
     * blurs the backdrop behind it (API 31+, [WindowManager.isCrossWindowBlurEnabled]); the
     * blur carries legibility, so the fill can stay translucent and let it show through.
     */
    val scrimOverlayBlur = Color.argb(209, 14, 15, 18)

    // ---- Rank-tier language (mirror components/hud rankTier) ----------------
    /** Map a 0–100 rank/score percentage to its tier colour. */
    fun tierColor(percent: Double): Int = when {
        percent >= 99 -> tierPerfect
        percent >= 95 -> tierGreat
        percent >= 88 -> tierGood
        percent >= 75 -> tierFair
        else -> tierWeak
    }

    /** Map a 0–100 rank/score percentage to its tier label. */
    fun tierLabel(percent: Double): String = when {
        percent >= 99 -> "ELITE"
        percent >= 95 -> "GREAT"
        percent >= 88 -> "GOOD"
        percent >= 75 -> "FAIR"
        else -> "WEAK"
    }

    // ---- Fonts --------------------------------------------------------------
    // Display: Chakra Petch · Body: Manrope · Numerics: JetBrains Mono.
    private val fontCache = HashMap<Int, Typeface>()

    private fun font(ctx: Context, resId: Int): Typeface =
        fontCache.getOrPut(resId) {
            runCatching { ResourcesCompat.getFont(ctx, resId) }.getOrNull() ?: Typeface.DEFAULT
        }

    /** Chakra Petch — headings, labels, buttons. weight 400/600/700. */
    fun display(ctx: Context, weight: Int = 600): Typeface = font(
        ctx,
        when {
            weight >= 700 -> R.font.chakra_petch_bold
            weight >= 600 -> R.font.chakra_petch_semibold
            else -> R.font.chakra_petch_regular
        }
    )

    /** Manrope — body copy. weight 400/600/700. */
    fun body(ctx: Context, weight: Int = 400): Typeface = font(
        ctx,
        when {
            weight >= 700 -> R.font.font_manrope_bold
            weight >= 600 -> R.font.font_manrope_semibold
            else -> R.font.font_manrope_regular
        }
    )

    /** JetBrains Mono — all figures (CP, IVs, ranks). Tabular by design. weight 400/500/700. */
    fun mono(ctx: Context, weight: Int = 700): Typeface = font(
        ctx,
        when {
            weight >= 700 -> R.font.font_jetbrains_bold
            weight >= 500 -> R.font.font_jetbrains_medium
            else -> R.font.font_jetbrains_regular
        }
    )

    // ---- Units --------------------------------------------------------------
    fun dp(ctx: Context, value: Float): Int =
        (value * ctx.resources.displayMetrics.density).toInt()

    fun dp(ctx: Context, value: Int): Int = dp(ctx, value.toFloat())

    // ---- Drawables ----------------------------------------------------------
    /** Flat slate card: fill + hairline border + rounded corners (radius in dp). */
    fun cardBackground(
        ctx: Context,
        radiusDp: Float = 16f,
        fill: Int = surfaceCard,
        stroke: Int = borderDefault,
        strokeDp: Float = 1f,
    ): GradientDrawable = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(fill)
        cornerRadius = dp(ctx, radiusDp).toFloat()
        setStroke(dp(ctx, strokeDp), stroke)
    }

    /**
     * Verdict overlay surface: larger radius, stronger border. Uses the lighter
     * [scrimOverlayBlur] fill when [blurred] (the window blurs the backdrop behind it),
     * otherwise the strong opaque [scrimOverlay] for legibility.
     */
    fun overlayCardBackground(ctx: Context, blurred: Boolean = false): GradientDrawable =
        cardBackground(
            ctx, radiusDp = 22f,
            fill = if (blurred) scrimOverlayBlur else scrimOverlay,
            stroke = borderStrong, strokeDp = 1f,
        )

    /** Gold primary button background (ripple over a rounded gold fill). */
    fun primaryButtonBackground(ctx: Context): RippleDrawable {
        val content = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(gold)
            cornerRadius = dp(ctx, 12f).toFloat()
        }
        return RippleDrawable(ColorStateList.valueOf(goldDeep), content, null)
    }

    /** Ghost/secondary button: transparent fill, hairline border, subtle ripple. */
    fun ghostButtonBackground(ctx: Context): RippleDrawable {
        val content = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.TRANSPARENT)
            cornerRadius = dp(ctx, 12f).toFloat()
            setStroke(dp(ctx, 1f), borderStrong)
        }
        return RippleDrawable(ColorStateList.valueOf(Color.argb(26, 255, 255, 255)), content, null)
    }

    /** Floating action button: dark radial fill with a gold findability ring. */
    fun fabBackground(ctx: Context, ringColor: Int = gold): GradientDrawable =
        GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            gradientType = GradientDrawable.RADIAL_GRADIENT
            gradientRadius = dp(ctx, 30f).toFloat()
            colors = intArrayOf(0xFF2B313D.toInt(), bgApp)
            setStroke(dp(ctx, 1.5f), ringColor)
        }

    // ---- View builders ------------------------------------------------------
    /** Display-font overline: small, UPPERCASE, wide tracking, tertiary colour. */
    fun overline(ctx: Context, text: String, color: Int = textTertiary): TextView =
        TextView(ctx).apply {
            this.text = text.uppercase()
            typeface = display(ctx, 600)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
            setTextColor(color)
            letterSpacing = 0.14f
        }

    /** Body paragraph in Manrope with comfortable line spacing. */
    fun bodyText(ctx: Context, text: String, color: Int = textSecondary, sizeSp: Float = 15f): TextView =
        TextView(ctx).apply {
            this.text = text
            typeface = body(ctx, 400)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp)
            setTextColor(color)
            setLineSpacing(0f, 1.45f)
        }

    /** Section heading in the display font. */
    fun heading(ctx: Context, text: String, sizeSp: Float = 20f): TextView =
        TextView(ctx).apply {
            this.text = text
            typeface = display(ctx, 700)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp)
            setTextColor(textPrimary)
            letterSpacing = 0.01f
        }

    /** A primary (gold) or ghost (outline) button, styled to the system. */
    fun button(ctx: Context, label: String, primary: Boolean, onClick: () -> Unit): TextView =
        TextView(ctx).apply {
            text = label
            typeface = display(ctx, 600)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(if (primary) textOnGold else textPrimary)
            letterSpacing = 0.02f
            gravity = Gravity.CENTER
            isClickable = true
            isFocusable = true
            minHeight = dp(ctx, 52)
            val padH = dp(ctx, 24)
            setPadding(padH, dp(ctx, 14), padH, dp(ctx, 14))
            background = if (primary) primaryButtonBackground(ctx) else ghostButtonBackground(ctx)
            setOnClickListener { onClick() }
        }

    /** Vertical spacer of [heightDp] dp, for use between views in a LinearLayout. */
    fun spacer(ctx: Context, heightDp: Int): View =
        View(ctx).apply {
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT, dp(ctx, heightDp)
            )
        }
}
