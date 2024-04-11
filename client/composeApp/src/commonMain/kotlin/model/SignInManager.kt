package model

import network.ApiInterface
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInManager(
    private val username: String, 
    private val password: String
) {
    fun signIn(onSuccess: () -> Unit) {
        val call = RetrofitClient.retrofit.create(ApiInterface::class.java)
        val registerInfo = UserSignInBody(username, password)
        call.loginUser(registerInfo).enqueue(object : Callback<network.User> {
            override fun onFailure(call: Call<network.User>, t: Throwable) {
                println("failure")
            }
            override fun onResponse(call: Call<network.User>, response: Response<network.User>) {
                if (response.code() == 200) {
                    println("success")
                    var user: User?
                    response.body().let { 
                        user = it?.convertUser()
                    }
                    AuthManager().saveAuthData(username, password, user)
                    onSuccess()
                } else {
                    println("response but wrong code")
                }
            }
        })
    }

    data class UserSignInBody(
        val username: String,
        val password: String
    )
}
