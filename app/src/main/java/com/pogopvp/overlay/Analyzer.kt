package com.pogopvp.overlay

/**
 * A creature's standing in one PvP league: the rank within the league's IV pool,
 * the percentile (drives the rank-tier colour), the capped CP, and the recommended
 * moveset. [Brand.tierColor]/[Brand.tierLabel] turn [percent] into the HUD colour language.
 */
data class LeagueStanding(
    val label: String,    // short overline, e.g. "GREAT", "ULTRA"
    val league: String,   // full name, e.g. "Great League"
    val rank: Int,
    val total: Int,
    val percent: Double,
    val cp: Int,
    val moves: String?,
)

/**
 * The structured result of one appraisal read, consumed by the overlay verdict card.
 * Numbers lead the hierarchy: identity + CP, the raw IV%, and each league standing.
 */
data class Verdict(
    val name: String,
    val cp: Int?,
    val ivAtk: Int,
    val ivDef: Int,
    val ivSta: Int,
    val recognized: Boolean,
    val leagues: List<LeagueStanding>,
) {
    val ivTotal: Int get() = ivAtk + ivDef + ivSta
    /** Raw IV percentage (0–100): how close to a perfect 15/15/15. */
    val ivPercent: Double get() = ivTotal / 45.0 * 100.0
    val isHundo: Boolean get() = ivAtk == 15 && ivDef == 15 && ivSta == 15
}

/**
 * Builds the overlay verdict from one appraisal-screen read: the OCR'd name + CP, the
 * pixel-read IV bars, and (for a recognised species) the Great/Ultra League rank plus
 * PvPoke's recommended moveset for that league.
 */
object Analyzer {

    /** Structured verdict for the HUD card. See [summarizeFull] for the flat-text form. */
    fun analyze(scan: ScanResult, iv: AppraisalResult): Verdict {
        val name = scan.name?.trim()?.takeIf { it.isNotEmpty() }
        val resolved = name?.let { Species.resolve(it, scan.types) }
        val base = resolved?.stats
        val leagues = if (base != null) {
            listOfNotNull(
                standing("GREAT", "Great League", base, iv, PvpCalculator.GREAT_LEAGUE, resolved.id),
                standing("ULTRA", "Ultra League", base, iv, PvpCalculator.ULTRA_LEAGUE, resolved.id),
            )
        } else {
            emptyList()
        }
        return Verdict(
            name = name ?: "Unknown",
            cp = scan.cp,
            ivAtk = iv.ivAtk,
            ivDef = iv.ivDef,
            ivSta = iv.ivSta,
            recognized = base != null,
            leagues = leagues,
        )
    }

    private fun standing(
        label: String,
        league: String,
        base: PvpCalculator.BaseStats,
        iv: AppraisalResult,
        cap: Int,
        speciesId: String?,
    ): LeagueStanding? {
        val r = PvpCalculator.rankOf(base, iv.ivAtk, iv.ivDef, iv.ivSta, cap) ?: return null
        return LeagueStanding(
            label = label,
            league = league,
            rank = r.rank,
            total = r.total,
            percent = r.percent,
            cp = r.cp,
            moves = speciesId?.let { Moves.forLeague(it, cap) },
        )
    }

    fun summarizeFull(scan: ScanResult, iv: AppraisalResult): String {
        val name = scan.name?.trim()?.takeIf { it.isNotEmpty() }
        val resolved = name?.let { Species.resolve(it, scan.types) }
        val base = resolved?.stats

        val sb = StringBuilder()
        sb.append(name ?: "Unknown")
        scan.cp?.let { sb.append("  CP ").append(it) }
        sb.append("\nIVs ").append(iv.ivAtk).append('/').append(iv.ivDef).append('/').append(iv.ivSta)

        if (base != null) {
            appendLeague(sb, "GL", base, iv, PvpCalculator.GREAT_LEAGUE, resolved.id)
            appendLeague(sb, "UL", base, iv, PvpCalculator.ULTRA_LEAGUE, resolved.id)
        } else {
            sb.append("\n(name not recognized — re-tap)")
        }
        return sb.toString()
    }

    private fun appendLeague(
        sb: StringBuilder,
        league: String,
        base: PvpCalculator.BaseStats,
        iv: AppraisalResult,
        cap: Int,
        speciesId: String?
    ) {
        val ranking = PvpCalculator.rankOf(base, iv.ivAtk, iv.ivDef, iv.ivSta, cap) ?: return
        sb.append('\n')
            .append(league).append(" rank ")
            .append(ranking.rank).append('/').append(ranking.total)
            .append(" (").append(String.format("%.1f", ranking.percent)).append("%)")
            .append("  CP ").append(ranking.cp)

        val moves = speciesId?.let { Moves.forLeague(it, cap) }
        if (moves != null) sb.append("\n  ").append(moves)
    }
}
