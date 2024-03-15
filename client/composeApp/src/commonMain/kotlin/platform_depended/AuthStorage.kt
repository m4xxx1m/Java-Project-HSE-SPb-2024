package platform_depended

expect object AuthStorage {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}
