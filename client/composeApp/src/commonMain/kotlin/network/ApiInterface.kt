package network

import model.SignInManager
import model.SignUpManager
import navigation.CreatePostManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @Headers("Content-Type:application/json")
    @POST("register")
    fun registerUser(@Body info: SignUpManager.UserSignUpBody): 
            Call<User>

    @Headers("Content-Type:application/json")
    @POST("login")
    fun loginUser(@Body info: SignInManager.UserSignInBody):
            Call<User>

    @Headers("Content-Type:application/json")
    @POST("post/add")
    fun createPost(@Body info: CreatePostManager.CreatePostBody):
            Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @GET("post/getAll")
    fun getAllPosts(): Call<List<Post>>

    @Headers("Content-Type:application/json")
    @GET("user/{userId}")
    fun getUser(@Path("userId") userId: Int): Call<User>

    @Headers("Content-Type:application/json")
    @POST("users/getUsersList")
    fun getUsersList(@Body userIds: Set<Int>): Call<List<User>>
    
    @Headers("Content-Type:application/json")
    @GET("/post/{postId}/comments")
    fun getComments(@Path("postId") postId: Int): Call<List<Comment>>

    @Headers("Content-Type:application/json")
    @GET("/post/{postId}")
    fun getPost(@Path("postId") postId: Int): Call<Post>

    @Headers("Content-Type:application/json")
    @POST("/post/{postId}/addComment")
    fun addComment(@Path("postId") postId: Int, @Body comment: CommentCreate): Call<Comment>
}
