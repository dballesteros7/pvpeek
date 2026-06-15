package com.pogopvp.overlay

/** What a single appraisal-screen scan yielded. */
data class ScanResult(
    val cp: Int?,
    val name: String?,
    /** Where [name] came from: "caught" (bottom line), "card" (top, may be a nickname), "none". */
    val nameSource: String,
    val types: List<String>,
    val raw: String
)

object ScanParser {

    // "CP 1456", "CP1456", "CP: 1456" — tolerate OCR's stray punctuation.
    private val CP_PATTERN = Regex("""CP\s*[:.]?\s*(\d{1,5})""", RegexOption.IGNORE_CASE)

    // The caught-info line ("This Greninja was caught on …") carries the REAL species name,
    // immune to nicknames shown on the card above. This is the preferred name source.
    private val CAUGHT_PATTERN = Regex("""This\s+(.+?)\s+was\s+caught""", RegexOption.IGNORE_CASE)

    private val TYPES = listOf(
        "normal", "fire", "water", "electric", "grass", "ice", "fighting", "poison",
        "ground", "flying", "psychic", "bug", "rock", "ghost", "dragon", "dark", "steel", "fairy"
    )

    fun parse(cpText: String, nameText: String, typeText: String, caughtText: String): ScanResult {
        // OCR loves to read the zero in a CP as the letter O.
        val cleaned = cpText.replace('O', '0').replace('o', '0')
        val cp = CP_PATTERN.find(cleaned)?.groupValues?.get(1)?.toIntOrNull()

        // Prefer the real species name from the caught-info line; fall back to the card name
        // (which may be a nickname) only if that line couldn't be read.
        val caughtName = CAUGHT_PATTERN.find(caughtText.replace('\n', ' '))
            ?.groupValues?.get(1)?.trim()?.takeIf { it.isNotEmpty() }
        val cardName = nameText.lineSequence()
            .map { it.trim() }
            .firstOrNull { it.length >= 3 && it.any(Char::isLetter) }
        val name = caughtName ?: cardName
        val nameSource = when {
            caughtName != null -> "caught"
            cardName != null -> "card"
            else -> "none"
        }

        val lowerTypes = typeText.lowercase()
        val types = TYPES.filter { lowerTypes.contains(it) }

        return ScanResult(
            cp = cp, name = name, nameSource = nameSource, types = types,
            raw = "CP[$cpText] CAUGHT[$caughtName] CARD[$cardName] TYPE[$typeText]"
        )
    }
}
