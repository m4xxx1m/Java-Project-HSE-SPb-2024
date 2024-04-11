package platform_depended

import java.util.prefs.Preferences

actual object AuthStorage {
    private val prefs = Preferences.userRoot().node("ru.hse.auth")

    actual fun saveToken(token: String) {
        prefs.put("authToken", token)
    }

    actual fun getToken(): String? {
        return prefs.get("authToken", null)
    }

    actual fun clearToken() {
        prefs.remove("authToken")
    }
}