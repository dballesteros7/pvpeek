package com.pogopvp.overlay

import android.content.Context
import com.pogopvp.overlay.PvpCalculator.BaseStats

/**
 * Species → base stats lookup.
 *
 * Ships with a tiny built-in dex so the scaffold runs end-to-end with zero setup, but once
 * [init] loads PvPoke's full gamemaster (≈1700 species) via [GameMaster] every lookup is
 * served from that table, with the built-in map kept only as a fallback.
 */
object Species {

    private val DEX: Map<String, BaseStats> = mapOf(
        "azumarill" to BaseStats(atk = 112, def = 152, sta = 225),
        "medicham" to BaseStats(atk = 121, def = 152, sta = 155),
        "bastiodon" to BaseStats(atk = 94, def = 286, sta = 155),
        "stunfisk" to BaseStats(atk = 144, def = 171, sta = 207), // Galarian
        "registeel" to BaseStats(atk = 143, def = 285, sta = 190),
        "swampert" to BaseStats(atk = 208, def = 175, sta = 225),
        "bulbasaur" to BaseStats(atk = 118, def = 111, sta = 128)
    )

    /** Loads the full gamemaster asset. Idempotent; safe to call more than once. */
    fun init(context: Context) = GameMaster.init(context)

    /** A resolved species: canonical speciesId (null for the built-in fallback) + base stats. */
    data class Resolved(val id: String?, val stats: BaseStats)

    fun match(name: String, types: List<String> = emptyList()): BaseStats? =
        resolve(name, types)?.stats

    /**
     * Loose match — OCR'd names are messy. Prefer the full gamemaster once it's loaded,
     * falling back to the tiny built-in dex if it isn't initialised or has no match.
     * [types] (from the appraisal screen) disambiguate regional forms with a shared name.
     */
    fun resolve(name: String, types: List<String> = emptyList()): Resolved? {
        if (GameMaster.isLoaded) {
            GameMaster.resolve(name, types)?.let { return Resolved(it.id, it.stats) }
        }
        val key = name.lowercase().filter(Char::isLetter)
        DEX[key]?.let { return Resolved(null, it) }
        return DEX.entries.firstOrNull { key.contains(it.key) || it.key.contains(key) }
            ?.let { Resolved(null, it.value) }
    }
}
