package network

import model.SignInManager
import model.SignUpManager
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterfacePreAuth {
    @Headers("Content-Type:application/json")
    @POST("/auth/sign-up")
    fun registerUser(@Body info: SignUpManager.UserSignUpBody):
            Call<AuthenticationResponse>

    @Headers("Content-Type:application/json")
    @POST("/auth/sign-in")
    fun loginUser(@Body info: SignInManager.UserSignInBody):
            Call<AuthenticationResponse>

    @Headers("Content-Type:application/json")
    @GET("/getTags")
    fun getTags(): Call<ArrayList<String>>
}
