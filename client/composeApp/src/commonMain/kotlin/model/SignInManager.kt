package model

import network.ApiInterface
import network.RetrofitClient
import okhttp3.ResponseBody
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
        call.loginUser(registerInfo).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("failure")
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 201) {
                    println("success")
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
