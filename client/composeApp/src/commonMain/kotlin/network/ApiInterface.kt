package network

import model.SignInManager
import model.SignUpManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import ui.CreatePostManager

interface ApiInterface {
    @Headers("Content-Type:application/json")
    @POST("register")
    fun registerUser(@Body info: SignUpManager.UserSignUpBody): 
            Call<ResponseBody>
    
    @Headers("Content-Type:application/json")
    @POST("login")
    fun loginUser(@Body info: SignInManager.UserSignInBody):
            Call<ResponseBody>
    
    @Headers("Content-Type:application/json")
    @POST("post/add")
    fun createPost(@Body info: CreatePostManager.CreatePostBody):
            Call<ResponseBody>
    
    @Headers("Content-Type:application/json")
    @GET("post/getAll")
    fun getAllPosts(): Call<ResponseBody>
}
