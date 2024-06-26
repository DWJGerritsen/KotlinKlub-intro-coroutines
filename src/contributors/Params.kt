package contributors

import java.util.prefs.Preferences

private fun prefNode(): Preferences = Preferences.userRoot().node("ContributorsUI")

data class Params(val username: String, val password: String, val org: String, val variant: Variant)

fun loadStoredParams(): Params {
    return prefNode().run {
        Params(
            get("username", "coroutines-demo"),
            get("password", "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"),
            get("org", "kotlin"),
            Variant.valueOf(get("variant", Variant.BLOCKING.name))
        )
    }
}

fun removeStoredParams() {
    prefNode().removeNode()
}

fun saveParams(params: Params) {
    prefNode().apply {
        put("username", params.username)
        put("password", params.password)
        put("org", params.org)
        put("variant", params.variant.name)
        sync()
    }
}
