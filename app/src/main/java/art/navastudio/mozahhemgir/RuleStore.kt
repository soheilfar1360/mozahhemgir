package art.navastudio.mozahhemgir

import android.content.Context

object RuleStore {
    private const val PREFS = "call_filter_rules"
    private const val KEY_PREFIXES = "blocked_prefixes"
    private const val KEY_ENABLED = "enabled"
    private const val KEY_BLOCKED_COUNT = "blocked_count"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun isEnabled(context: Context): Boolean =
        prefs(context).getBoolean(KEY_ENABLED, true)

    fun setEnabled(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_ENABLED, enabled).apply()
    }

    fun prefixes(context: Context): Set<String> {
        val stored = prefs(context).getStringSet(KEY_PREFIXES, null)
        return (stored ?: RuleCatalog.defaultPrefixes(context)).toSet()
    }

    fun addPrefix(context: Context, raw: String): Boolean {
        val prefix = NumberNormalizer.normalizePrefix(raw) ?: return false
        val next = prefixes(context).toMutableSet()
        val added = next.add(prefix)
        if (added) prefs(context).edit().putStringSet(KEY_PREFIXES, next).apply()
        return added
    }

    fun removePrefix(context: Context, prefix: String) {
        val next = prefixes(context).toMutableSet()
        next.remove(prefix)
        prefs(context).edit().putStringSet(KEY_PREFIXES, next).apply()
    }

    fun shouldBlock(context: Context, rawNumber: String?): Boolean {
        if (!isEnabled(context)) return false
        val normalized = NumberNormalizer.toIranDomestic(rawNumber) ?: return false
        return prefixes(context).any { normalized.startsWith(it) }
    }

    fun incrementBlockedCount(context: Context) {
        val p = prefs(context)
        p.edit().putLong(KEY_BLOCKED_COUNT, p.getLong(KEY_BLOCKED_COUNT, 0L) + 1L).apply()
    }

    fun blockedCount(context: Context): Long =
        prefs(context).getLong(KEY_BLOCKED_COUNT, 0L)
}
