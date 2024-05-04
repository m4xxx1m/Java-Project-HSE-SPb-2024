package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import model.AuthManager
import network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.NewPostForm

object CreatePostTab: Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Rounded.AddCircle)
            val title = "Create Post"
            val index: UShort = 2u
            return TabOptions(index, title, icon)
        }

    @Composable
    override fun Content() {
        NewPostForm()
    }
}

class CreatePostManager {
    fun createPost(title: String, text: String, tags: List<Int>, clearFields: () -> Unit) {
//        val retrofitCall = RetrofitClient.retrofit.create(ApiInterface::class.java)
        val retrofitCall = RetrofitClient.retrofitCall
        val createPostInfo = CreatePostBody(AuthManager.currentUser.id, title, text, emptyList())
        retrofitCall.createPost(createPostInfo).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("failure on creating post")
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 201) {
                    println("successfully created post")
                } else {
                    println("response but wrong code on creating post")
                }
                clearFields()
            }
        })
    }

    data class CreatePostBody(
        val authorId: Int,
        val title: String,
        val content: String,
        val tagIds: List<Int>
    )
}
