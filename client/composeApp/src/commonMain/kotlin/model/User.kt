package model

import androidx.compose.runtime.MutableState
import network.RetrofitClient
import network.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class User(
    val id: Int, 
    val name: String, 
    val imageUrl: String?
) {
    fun setProfile(userProfile: MutableState<UserProfile?>) {
        val retrofitCall = RetrofitClient.retrofitCall
        retrofitCall.getUser(id).enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 200) {
                    response.body()?.let {
                        userProfile.value = UserProfile(
                            user = this@User,
                            contacts = it.contacts,
                            about = it.bio,
                            tags = it.tags,
                            cvFileName = it.resumeFileName
                        )
                    }
                } else {
                    println("wrong code while getting user profile")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                println("failure while getting user profile")
            }
        })
    }
}
