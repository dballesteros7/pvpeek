package com.pogopvp.overlay

import android.content.Context
import org.json.JSONObject

/**
 * PvPoke's recommended movesets per league, loaded from `assets/recommended_moves.json`
 * (keyed by speciesId, so it composes with the variant detection — Galarian Corsola gets
 * its own moveset). Each value is a display string: "Fast · Charged1 / Charged2".
 */
object Moves {

    private val gl = HashMap<String, String>()
    private val ul = HashMap<String, String>()

    @Volatile
    var isLoaded: Boolean = false
        private set

    @Synchronized
    fun init(context: Context) {
        if (isLoaded) return
        val text = context.assets.open("recommended_moves.json")
            .bufferedReader(Charsets.UTF_8).use { it.readText() }
        val obj = JSONObject(text)
        val keys = obj.keys()
        while (keys.hasNext()) {
            val id = keys.next()
            val o = obj.getJSONObject(id)
            o.optString("gl").takeIf { it.isNotEmpty() }?.let { gl[id] = it }
            o.optString("ul").takeIf { it.isNotEmpty() }?.let { ul[id] = it }
        }
        isLoaded = true
    }

    /** Recommended moveset for [speciesId] in the league with this CP [cap], or null. */
    fun forLeague(speciesId: String, cap: Int): String? = when (cap) {
        PvpCalculator.GREAT_LEAGUE -> gl[speciesId]
        PvpCalculator.ULTRA_LEAGUE -> ul[speciesId]
        else -> null
    }
}
