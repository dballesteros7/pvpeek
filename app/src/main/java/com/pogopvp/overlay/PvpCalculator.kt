package com.pogopvp.overlay

import kotlin.math.floor
import kotlin.math.max
import kotlin.math.sqrt

/**
 * The PvP ranking engine — the part the website does by hand.
 *
 * Given a species' base stats and a league CP cap, it scores every one of the 4096
 * IV combinations (0..15 per stat) at the highest level that stays under the cap, then
 * ranks them by **stat product** (the standard PvP quality metric). Rank 1 is the
 * bulk-optimal spread for that league.
 *
 * NOTE ON DATA: the CPM table below is the standard Niantic level→multiplier curve for
 * whole and half levels 1–40, plus whole levels 41–51 (XL candy). For production, load
 * CPM and base stats from PvPoke's gamemaster.json so the dex stays current — treat the
 * constants here as a runnable reference, not a maintained source of truth.
 */
object PvpCalculator {

    data class BaseStats(val atk: Int, val def: Int, val sta: Int)

    data class IvSpread(
        val level: Double,
        val ivAtk: Int,
        val ivDef: Int,
        val ivSta: Int,
        val cp: Int,
        val statProduct: Double
    )

    /** CP = floor( Atk * sqrt(Def) * sqrt(Sta) * CPM^2 / 10 ), floored at 10. */
    fun cp(base: BaseStats, ivA: Int, ivD: Int, ivS: Int, cpm: Double): Int {
        val atk = base.atk + ivA
        val def = base.def + ivD
        val sta = base.sta + ivS
        val raw = atk * sqrt(def.toDouble()) * sqrt(sta.toDouble()) * cpm * cpm / 10.0
        return max(10, floor(raw).toInt())
    }

    fun statProduct(base: BaseStats, ivA: Int, ivD: Int, ivS: Int, cpm: Double): Double {
        val atk = (base.atk + ivA) * cpm
        val def = (base.def + ivD) * cpm
        val hp = floor((base.sta + ivS) * cpm)
        return atk * def * hp
    }

    /** Highest-level spread for these IVs that still fits under [cap], or null if even L1 exceeds it. */
    fun bestSpreadUnderCap(base: BaseStats, ivA: Int, ivD: Int, ivS: Int, cap: Int): IvSpread? {
        var best: IvSpread? = null
        for ((level, cpm) in CPM) { // ascending: once we pass the cap we can stop
            if (level > MAX_RANK_LEVEL) break // ignore the Best Buddy-only level 51
            val c = cp(base, ivA, ivD, ivS, cpm)
            if (c <= cap) {
                best = IvSpread(level, ivA, ivD, ivS, c, statProduct(base, ivA, ivD, ivS, cpm))
            } else {
                break
            }
        }
        return best
    }

    /** All 4096 IV spreads at their cap-maxed level, ranked best stat product first. */
    fun rankAllUnderCap(base: BaseStats, cap: Int): List<IvSpread> {
        val out = ArrayList<IvSpread>(4096)
        for (a in 0..15) for (d in 0..15) for (s in 0..15) {
            bestSpreadUnderCap(base, a, d, s, cap)?.let(out::add)
        }
        out.sortByDescending { it.statProduct }
        return out
    }

    /** Where a specific IV spread lands in a league: its rank, the field size, the % of rank-1's bulk, and the cap-maxed CP. */
    data class Ranking(val rank: Int, val total: Int, val percent: Double, val cp: Int)

    /**
     * Rank a concrete IV spread within [cap]'s 4096-deep ladder. [percent] compares this
     * spread's stat product against rank-1's (so rank 1 == 100.0%). Returns null when the
     * spread can't even reach level 1 under the cap (so it never appears in the ladder).
     */
    fun rankOf(base: BaseStats, ivA: Int, ivD: Int, ivS: Int, cap: Int): Ranking? {
        val ranked = rankAllUnderCap(base, cap)
        if (ranked.isEmpty()) return null
        val index = ranked.indexOfFirst { it.ivAtk == ivA && it.ivDef == ivD && it.ivSta == ivS }
        if (index < 0) return null
        val spread = ranked[index]
        val best = ranked.first().statProduct
        val percent = if (best > 0.0) spread.statProduct / best * 100.0 else 0.0
        return Ranking(rank = index + 1, total = ranked.size, percent = percent, cp = spread.cp)
    }

    // League CP caps.
    const val GREAT_LEAGUE = 1500
    const val ULTRA_LEAGUE = 2500

    // Rank as if level 50 is the ceiling (level 51 needs Best Buddy, so most mons can't hit it).
    private const val MAX_RANK_LEVEL = 50.0

    private val CPM: List<Pair<Double, Double>> = listOf(
        1.0 to 0.094, 1.5 to 0.1351374318, 2.0 to 0.16639787, 2.5 to 0.192650919,
        3.0 to 0.21573247, 3.5 to 0.2365726613, 4.0 to 0.25572005, 4.5 to 0.2735303812,
        5.0 to 0.29024988, 5.5 to 0.3048308495, 6.0 to 0.3210876, 6.5 to 0.3354450362,
        7.0 to 0.34921268, 7.5 to 0.3625892521, 8.0 to 0.3752356, 8.5 to 0.387592416,
        9.0 to 0.39956728, 9.5 to 0.4111935514, 10.0 to 0.4225, 10.5 to 0.4329264091,
        11.0 to 0.44310755, 11.5 to 0.4530599591, 12.0 to 0.46279839, 12.5 to 0.4723360159,
        13.0 to 0.48168495, 13.5 to 0.4908558003, 14.0 to 0.49985844, 14.5 to 0.508701765,
        15.0 to 0.51739395, 15.5 to 0.5259425113, 16.0 to 0.5343543, 16.5 to 0.5426357375,
        17.0 to 0.5507927, 17.5 to 0.5588305862, 18.0 to 0.5667545, 18.5 to 0.5745691333,
        19.0 to 0.5822789, 19.5 to 0.5898879072, 20.0 to 0.5974, 20.5 to 0.6048236651,
        21.0 to 0.6121573, 21.5 to 0.6194041216, 22.0 to 0.6265671, 22.5 to 0.6336491432,
        23.0 to 0.64065295, 23.5 to 0.6475809666, 24.0 to 0.65443563, 24.5 to 0.6612192524,
        25.0 to 0.667934, 25.5 to 0.6745818959, 26.0 to 0.6811649, 26.5 to 0.6876849038,
        27.0 to 0.69414365, 27.5 to 0.70054287, 28.0 to 0.7068842, 28.5 to 0.7131691091,
        29.0 to 0.7193991, 29.5 to 0.7255756136, 30.0 to 0.7317, 30.5 to 0.7347410093,
        31.0 to 0.7377695, 31.5 to 0.7407855938, 32.0 to 0.74378943, 32.5 to 0.7467812109,
        33.0 to 0.74976104, 33.5 to 0.7527290867, 34.0 to 0.7556855, 34.5 to 0.7586303683,
        35.0 to 0.76156384, 35.5 to 0.7644860647, 36.0 to 0.76739717, 36.5 to 0.7702972656,
        37.0 to 0.7731865, 37.5 to 0.7760649616, 38.0 to 0.77893275, 38.5 to 0.7817900548,
        39.0 to 0.78463697, 39.5 to 0.7874736075, 40.0 to 0.7903,
        41.0 to 0.79530001, 42.0 to 0.80030001, 43.0 to 0.80530001, 44.0 to 0.81030001,
        45.0 to 0.81530001, 46.0 to 0.82030001, 47.0 to 0.82530001, 48.0 to 0.83030001,
        49.0 to 0.83530001, 50.0 to 0.84029999, 51.0 to 0.84529999
    )
}
