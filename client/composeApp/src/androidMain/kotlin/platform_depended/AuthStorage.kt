package platform_depended

import android.content.Context
import android.content.SharedPreferences

actual object AuthStorage {
    private const val PATH_NAME = "ru.hse.internfon.auth"
    private const val KEY = "ru.hse.internfon.auth.token.key"
    
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PATH_NAME, Context.MODE_PRIVATE)
    }
    
    private var sharedPreferences: SharedPreferences? = null
    
    actual fun saveToken(token: String) {
        sharedPreferences?.edit()?.putString(KEY, token)?.apply()
    }

    actual fun getToken(): String? {
        return sharedPreferences?.getString(KEY, null)
    }

    actual fun clearToken() {
        sharedPreferences?.edit()?.remove(KEY)?.apply()
    }
}
