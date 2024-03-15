package platform_depended

import android.content.Context
import android.content.SharedPreferences

actual object AuthStorage {
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
    }
    
    private var sharedPreferences: SharedPreferences? = null
    
    actual fun saveToken(token: String) {
        sharedPreferences?.edit()?.putString("authToken", token)?.apply()
    }

    actual fun getToken(): String? {
        return sharedPreferences?.getString("authToken", null)
    }

    actual fun clearToken() {
        sharedPreferences?.edit()?.remove("authToken")?.apply()
    }
}
