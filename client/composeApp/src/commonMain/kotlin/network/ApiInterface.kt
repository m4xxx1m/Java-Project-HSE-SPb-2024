package network

import model.SignInManager
import model.SignUpManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    @Headers("Content-Type:application/json")
    @POST("register")
    fun registerUser(@Body info: SignUpManager.UserSignUpBody): 
            Call<ResponseBody>
    
    @Headers("Content-Type:application/json")
    @POST("login")
    fun loginUser(@Body info: SignInManager.UserSignInBody):
            Call<ResponseBody>
}
