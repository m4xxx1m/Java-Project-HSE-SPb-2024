package network

import navigation.CreatePostManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @Headers("Content-Type:application/json")
    @POST("/post/add")
    fun createPost(@Body info: CreatePostManager.CreatePostBody):
            Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @GET("/post/getAll")
    fun getAllPosts(): Call<List<Post>>

    @Headers("Content-Type:application/json")
    @GET("/users/getUser/{userId}")
    fun getUser(@Path("userId") userId: Int): Call<User>

    @Headers("Content-Type:application/json")
    @POST("/users/getUsersList")
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

//    @Headers("Content-Type:application/json")
//    @GET("/getTags")
//    fun getTags(): Call<ArrayList<String>>

    @Headers("Content-Type:application/json")
    @POST("/post/{postId}/like")
    fun likePost(@Path("postId") postId: Int, @Query("userId") userId: Int): Call<Int>

    @Headers("Content-Type:application/json")
    @POST("/post/{postId}/dislike")
    fun dislikePost(@Path("postId") postId: Int, @Query("userId") userId: Int): Call<Int>

    @Headers("Content-Type:application/json")
    @POST("/post/{postId}/comments/{commentId}/like")
    fun likeComment(
        @Path("postId") postId: Int,
        @Path("commentId") commentId: Int,
        @Query("userId") userId: Int
    ): Call<Int>

    @Headers("Content-Type:application/json")
    @POST("/post/{postId}/comments/{commentId}/dislike")
    fun dislikeComment(
        @Path("postId") postId: Int,
        @Path("commentId") commentId: Int,
        @Query("userId") userId: Int
    ): Call<Int>

    @Headers("Content-Type:application/json")
    @POST("/users/{userId}/updateContacts")
    fun updateContacts(@Path("userId") userId: Int, @Query("contacts") contacts: String): Call<Void>

    @Headers("Content-Type:application/json")
    @POST("/users/{userId}/updateBio")
    fun updateBio(@Path("userId") userId: Int, @Query("bio") bio: String): Call<Void>

    @Headers("Content-Type:application/json")
    @POST("/users/{subscriberId}/subscribe/{subscribeToId}")
    fun subscribe(
        @Path("subscriberId") subscriberId: Int,
        @Path("subscribeToId") subscribeToId: Int
    ): Call<Any>

    @Headers("Content-Type:application/json")
    @POST("/users/{subscriberId}/unsubscribe/{subscribeToId}")
    fun unsubscribe(
        @Path("subscriberId") subscriberId: Int,
        @Path("subscribeToId") subscribeToId: Int
    ): Call<Void>

    @Headers("Content-Type:application/json")
    @GET("/users/{subscriberId}/checkSubscription/{subscribeToId}")
    fun checkSubscription(
        @Path("subscriberId") subscriberId: Int,
        @Path("subscribeToId") subscribeToId: Int
    ): Call<Boolean>

    @Headers("Content-Type:application/json")
    @GET("/users/{subscriberId}/getSubscriptions")
    fun getSubscriptions(
        @Path("subscriberId") subscriberId: Int
    ): Call<List<User>>

    @Headers("Content-Type:application/json")
    @GET("/users/{userId}/getPostsFromSubscriptions")
    fun getPostsFromSubscriptions(@Path("userId") userId: Int): Call<List<Post>>

//    @Headers("Content-Type:application/json")
//    @GET("/users/{userId}/getUserTags")
//    fun getUserTags(@Path("userId") userId: Int): Call<String>

    @Headers("Content-Type:application/json")
    @POST("/users/{userId}/updateTags")
    fun updateUserTags(@Path("userId") userId: Int, @Query("tags") tags: String): Call<Void>

    @Headers("Content-Type:application/json")
    @POST("/post/{postId}/save")
    fun savePost(@Path("postId") postId: Int, @Query("userId") userId: Int): Call<Void>

    @Headers("Content-Type:application/json")
    @POST("/post/{postId}/delete")
    fun deletePost(@Path("postId") postId: Int): Call<Void>

    @Headers("Content-Type:application/json")
    @POST("post/{postId}/comments/{commendId}/delete")
    fun deleteComment(@Path("postId") postId: Int, @Path("commendId") commentId: Int): Call<Void>
    
    @Headers("Content-Type:application/json")
    @GET("/saved/posts")
    fun getSavedPosts(@Query("userId") userId: Int): Call<List<Post>>

    @Headers("Content-Type:application/json")
    @GET("/user/posts")
    fun getUserPosts(@Query("userId") userId: Int): Call<List<Post>>
}
