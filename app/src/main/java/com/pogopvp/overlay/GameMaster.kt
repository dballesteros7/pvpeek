package com.pogopvp.overlay

import android.content.Context
import com.pogopvp.overlay.PvpCalculator.BaseStats
import org.json.JSONArray

/**
 * Full Pokédex loaded at runtime from `assets/gamemaster_pokemon.json` — a slim projection
 * of PvPoke's gamemaster (≈1700 species: id/name/atk/def/sta/types).
 *
 * Regional variants share a base name in GO's UI ("Corsola" for both the normal Water/Rock
 * and the Galarian Ghost form), so a name-only lookup can't tell them apart. We group forms
 * by base name and disambiguate with the detected TYPE(S) from the appraisal screen.
 */
object GameMaster {

    private data class Entry(val id: String, val name: String, val stats: BaseStats, val types: List<String>)

    /** A resolved species: its canonical speciesId (for move lookups) and base stats. */
    data class Resolved(val id: String, val stats: BaseStats)

    /** Regional form words GO shows as a prefix but the gamemaster encodes as an id suffix. */
    private val FORM_WORDS = listOf("galarian", "alolan", "hisuian", "paldean")

    private val byName = HashMap<String, Entry>()
    private val byId = HashMap<String, Entry>()
    private val entries = ArrayList<Entry>()
    /** base species key (form words stripped) -> all its forms. */
    private val groups = HashMap<String, MutableList<Entry>>()

    @Volatile
    var isLoaded: Boolean = false
        private set

    /** Lowercase alphanumerics, accents/spaces/punctuation removed. */
    fun normalize(raw: String): String {
        val decomposed = java.text.Normalizer.normalize(raw, java.text.Normalizer.Form.NFD)
        return buildString(decomposed.length) {
            for (c in decomposed.lowercase()) if (c in 'a'..'z' || c in '0'..'9') append(c)
        }
    }

    private fun baseKey(norm: String): String {
        var b = norm
        for (f in FORM_WORDS) b = b.replace(f, "")
        return b
    }

    @Synchronized
    fun init(context: Context) {
        if (isLoaded) return
        val text = context.assets.open("gamemaster_pokemon.json")
            .bufferedReader(Charsets.UTF_8).use { it.readText() }
        val arr = JSONArray(text)
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val id = o.getString("id")
            val name = o.getString("name")
            val stats = BaseStats(o.getInt("atk"), o.getInt("def"), o.getInt("sta"))
            val typesArr = o.optJSONArray("types")
            val types = if (typesArr == null) emptyList() else
                (0 until typesArr.length()).map { typesArr.getString(it).lowercase() }
            val entry = Entry(id, name, stats, types)
            entries.add(entry)
            byName.putIfAbsent(normalize(name), entry)
            byId.putIfAbsent(normalize(id), entry)
            groups.getOrPut(baseKey(normalize(name))) { ArrayList() }.add(entry)
        }
        isLoaded = true
    }

    fun match(name: String): BaseStats? = match(name, emptyList())
    fun match(name: String, types: List<String>): BaseStats? = matchEntry(name, types)?.stats

    /** Like [match] but also returns the canonical speciesId, for move lookups. */
    fun resolve(name: String, types: List<String>): Resolved? =
        matchEntry(name, types)?.let { Resolved(it.id, it.stats) }

    /**
     * Resolve a species, using [types] (e.g. ["ghost"]) to pick the right regional form when
     * several share a base name. Priority: a form named in the input, then a type match, then
     * the base (non-form) entry.
     */
    private fun matchEntry(name: String, types: List<String>): Entry? {
        val key = normalize(name)
        if (key.isEmpty()) return null

        var base = key
        var inputForm: String? = null
        for (f in FORM_WORDS) if (base.contains(f)) { base = base.replace(f, ""); inputForm = f }

        groups[base]?.let { cands ->
            if (cands.size == 1) return cands[0]
            if (inputForm != null) {
                cands.firstOrNull { normalize(it.id).contains(inputForm!!) }?.let { return it }
            }
            if (types.isNotEmpty()) {
                val want = types.map { it.lowercase() }.toSet()
                var best: Entry? = null
                var bestOverlap = 0
                for (c in cands) {
                    val overlap = c.types.count { it in want }
                    if (overlap > bestOverlap) { bestOverlap = overlap; best = c }
                }
                best?.let { return it }
            }
            // Default to the base form (id without a regional suffix), else the first.
            cands.firstOrNull { e -> FORM_WORDS.none { normalize(e.id).contains(it) } }?.let { return it }
            return cands[0]
        }

        // No group hit (OCR slop): exact, then a forgiving substring pass.
        byName[key]?.let { return it }
        byId[key]?.let { return it }
        return entries.firstOrNull { e ->
            val n = normalize(e.name); key.contains(n) || n.contains(key)
        } ?: entries.firstOrNull { e ->
            val n = normalize(e.id); key.contains(n) || n.contains(key)
        }
    }
}
