package model

import network.AuthenticationResponse
import network.RetrofitClientPreAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInManager(
    private val username: String,
    private val password: String
) {
    fun signIn(onError: (() -> Unit)? = null, onSuccess: () -> Unit) {
//        val call = RetrofitClient.retrofit.create(ApiInterface::class.java)
        val retrofitCall = RetrofitClientPreAuth.retrofitCall
        val registerInfo = UserSignInBody(username, password)
        retrofitCall.loginUser(registerInfo).enqueue(object : Callback<AuthenticationResponse> {
            override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
                println("failure")
                onError?.let {
                    it()
                }
            }

            override fun onResponse(
                call: Call<AuthenticationResponse>,
                response: Response<AuthenticationResponse>
            ) {
                if (response.code() == 200) {
                    var user: User?
                    response.body().let {
                        user = it?.user?.convertUser()
                    }
                    val token = response.body()!!.token
                    AuthManager().saveAuthData(username, password, user, token)
                    onSuccess()
                } else {
                    println("response but wrong code")
                    onError?.let {
                        it()
                    }
                }
            }
        })
    }

    data class UserSignInBody(
        val username: String,
        val password: String
    )
}
