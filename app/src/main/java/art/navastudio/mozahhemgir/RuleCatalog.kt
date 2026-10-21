package art.navastudio.mozahhemgir

import android.content.Context
import org.json.JSONObject

object RuleCatalog {
    private const val ASSET_NAME = "iran.json"
    private val FALLBACK_PREFIXES = setOf("0942")

    /**
     * Loads community-maintained default prefixes packaged with the app.
     * Invalid or overly broad values are discarded by the same normalizer used by the UI.
     * If the catalog is absent or malformed, the app falls back to the original 0942 rule.
     */
    fun defaultPrefixes(context: Context): Set<String> = try {
        val raw = context.assets.open(ASSET_NAME).bufferedReader(Charsets.UTF_8).use { it.readText() }
        val json = JSONObject(raw)
        val values = json.getJSONArray("defaultBlockedPrefixes")
        buildSet {
            for (index in 0 until values.length()) {
                NumberNormalizer.normalizePrefix(values.optString(index))?.let(::add)
            }
        }.ifEmpty { FALLBACK_PREFIXES }
    } catch (_: Throwable) {
        FALLBACK_PREFIXES
    }
}
