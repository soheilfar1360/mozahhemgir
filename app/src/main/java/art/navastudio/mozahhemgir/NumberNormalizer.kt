package art.navastudio.mozahhemgir

object NumberNormalizer {
    /**
     * Converts common Iranian phone-number forms to domestic 0-prefixed form.
     * Examples:
     * +98942...   -> 0942...
     * 0098942...  -> 0942...
     * 98942...    -> 0942...
     * 942...      -> 0942... (only for 10-digit Iranian mobile-like numbers)
     */
    fun toIranDomestic(raw: String?): String? {
        if (raw.isNullOrBlank()) return null

        val digits = raw.filter(Char::isDigit)
        if (digits.isBlank()) return null

        return when {
            digits.startsWith("0098") && digits.length > 4 -> "0" + digits.drop(4)
            digits.startsWith("98") && digits.length > 2 -> "0" + digits.drop(2)
            digits.startsWith("0") -> digits
            digits.length == 10 && digits.startsWith("9") -> "0$digits"
            else -> digits
        }
    }

    fun normalizePrefix(raw: String): String? {
        val digits = raw.filter(Char::isDigit)
        if (digits.isBlank()) return null

        val normalized = when {
            digits.startsWith("0098") && digits.length > 4 -> "0" + digits.drop(4)
            digits.startsWith("98") && digits.length > 2 -> "0" + digits.drop(2)
            digits.startsWith("0") -> digits
            digits.startsWith("9") -> "0$digits"
            else -> digits
        }
        return normalized.takeIf { it.matches(Regex("^0\\d{3,10}$")) }
    }
}
