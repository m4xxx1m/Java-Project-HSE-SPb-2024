package model

import cafe.adriel.voyager.navigator.Navigator
import navigation.ConnectionErrorScreen
import navigation.MainNavigation
import platform_depended.AuthStorage

class AuthManager {
    companion object {
        private var currentUser_: User? = null
        val currentUser: User
            get() = currentUser_!!
    }

    fun tryLogin(navigator: Navigator, onError: (() -> Unit)?) : Boolean {
        val token = AuthStorage.getToken()
        if (token != null) {
            val tokenList = token.split('|')
            if (tokenList.size != 2) {
                AuthStorage.clearToken()
                return false
            }
            val login = tokenList[0]
            val password = tokenList[1]
            val signInManager = SignInManager(login, password)
            signInManager.signIn(onError) {
                navigator.popAll()
                navigator.replace(MainNavigation())
            }
            return true
        }
        return false
    }

    fun logOut(navigator: Navigator) {
        AuthStorage.clearToken()
        currentUser_ = null
        navigator.popAll()
        navigator.replace(ConnectionErrorScreen())
    }

    fun saveAuthData(login: String, password: String, user: User?) {
        if (AuthStorage.getToken() == null) {
            AuthStorage.saveToken("$login|$password")
        }
        currentUser_ = user
    }
}
