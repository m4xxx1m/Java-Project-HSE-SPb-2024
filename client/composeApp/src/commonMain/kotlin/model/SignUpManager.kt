package model

import network.ApiInterface
import network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpManager(private val email: String) {
    private var userName = ""
    private var password = ""

    fun initData(userName: String, password: String) {
        this.userName = userName
        this.password = password
    }

    fun signUp(onSuccess: () -> Unit) {
        val call = RetrofitClient.retrofit.create(ApiInterface::class.java)
        val registerInfo = UserSignUpBody(userName, email, password)
        call.registerUser(registerInfo).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("failure")
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    println("success")
                    onSuccess()
                } else {
                    println("response but wrong code")
                }
            }
        })
    }

    data class UserSignUpBody(
        val username: String,
        val email: String,
        val password: String
    )
}
