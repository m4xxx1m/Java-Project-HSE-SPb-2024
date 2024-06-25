package platform_depended

import java.util.prefs.Preferences

actual object AuthStorage {
    private const val PATH_NAME = "ru.hse.internfon.auth"
    private const val KEY = "ru.hse.internfon.auth.token.key"

    private val prefs = Preferences.userRoot().node(PATH_NAME)

    actual fun saveToken(token: String) {
        prefs.put(KEY, token)
    }

    actual fun getToken(): String? {
        return prefs.get(KEY, null)
    }

    actual fun clearToken() {
        prefs.remove(KEY)
    }
}
