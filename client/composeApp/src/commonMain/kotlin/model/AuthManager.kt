package model

import cafe.adriel.voyager.navigator.Navigator
import navigation.MainNavigation
import platform_depended.AuthStorage

class AuthManager {
    fun tryLogin(navigator: Navigator) {
        val token = AuthStorage.getToken()
        if (token != null) {
            val tokenList = token.split('|')
            if (tokenList.size != 2) {
                return
            }
            val login = tokenList[0]
            val password = tokenList[1]
            val signInManager = SignInManager(login, password)
            signInManager.signIn {
                navigator.popAll()
                navigator.replace(MainNavigation())
            }
        }
    }

    fun saveAuthData(login: String, password: String) {
        if (AuthStorage.getToken() == null) {
            AuthStorage.saveToken("$login|$password")
        }
    }
}
