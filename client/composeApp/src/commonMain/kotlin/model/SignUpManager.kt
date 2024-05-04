package model

import network.RetrofitClient
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
//        val call = RetrofitClient.retrofit.create(ApiInterface::class.java)
        val retrofitCall = RetrofitClient.retrofitCall
        val registerInfo = UserSignUpBody(userName, email, password)
        retrofitCall.registerUser(registerInfo).enqueue(object : Callback<network.User> {
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
                    AuthManager().saveAuthData(userName, password, user)
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
