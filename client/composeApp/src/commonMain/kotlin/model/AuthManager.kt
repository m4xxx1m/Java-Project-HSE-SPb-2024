package model

import cafe.adriel.voyager.navigator.Navigator
import navigation.ConnectionErrorScreen
import navigation.MainNavigation
import network.RetrofitClient
import network.RetrofitClientPreAuth
import platform_depended.AuthStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthManager {
    companion object {
        private var currentUser_: User? = null
        val currentUser: User
            get() = currentUser_!!
        
        var refreshHomeTab = true
    }

    fun tryLogin(navigator: Navigator, onFailure: (() -> Unit)?, onError: (() -> Unit)?) {
        val tokenString = AuthStorage.getToken()
        if (tokenString != null) {
            val tokenList = tokenString.split(' ')
            if (tokenList.size != 2) {
                AuthStorage.clearToken()
                onError?.invoke()
                return
            }
            val userId = try {
                tokenList[0].toInt()
            } catch (e: NumberFormatException) {
                AuthStorage.clearToken()
                onError?.invoke()
                return
            }
            val token = tokenList[1]
            RetrofitClientPreAuth.retrofitCall.getUser("Bearer $token", userId)
                .enqueue(object :
                    Callback<network.User> {
                    override fun onResponse(
                        call: Call<network.User>,
                        response: Response<network.User>
                    ) {
                        if (response.code() == 200) {
                            saveAuthData(response.body()?.convertUser(), token)
                            navigator.popAll()
                            navigator.replace(MainNavigation())
                        } else {
                            println("wrong code while getting user via token")
                            AuthStorage.clearToken()
                            onError?.invoke()
                        }
                    }

                    override fun onFailure(call: Call<network.User>, t: Throwable) {
                        println("failure while getting user via token")
                        onFailure?.invoke()
                    }

                })
        } else {
            onError?.invoke()
        }
    }

    fun logOut(navigator: Navigator) {
        refreshHomeTab = true
        AuthStorage.clearToken()
        currentUser_ = null
        navigator.popAll()
        navigator.replace(ConnectionErrorScreen())
    }

    fun saveAuthData(user: User?, token: String) {
        RetrofitClient.setToken(token)
        if (AuthStorage.getToken() == null && user != null) {
            AuthStorage.saveToken("${user.id} $token")
        }
        currentUser_ = user
    }
}
