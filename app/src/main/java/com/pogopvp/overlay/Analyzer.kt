package com.pogopvp.overlay

/**
 * Builds the overlay verdict from one appraisal-screen read: the OCR'd name + CP, the
 * pixel-read IV bars, and (for a recognised species) the Great/Ultra League rank plus
 * PvPoke's recommended moveset for that league.
 */
object Analyzer {

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
