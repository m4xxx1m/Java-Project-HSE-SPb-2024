package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import model.AuthManager
import network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.NewPostForm
import java.io.File

object CreatePostTab: Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Rounded.AddCircle)
            val title = "Создать пост"
            val index: UShort = 2u
            return TabOptions(index, title, icon)
        }

    @Composable
    override fun Content() {
        NewPostForm()
    }
}

class CreatePostManager {
    fun createPost(title: String, text: String, tags: String, file: File?, clearFields: () -> Unit) {
        val retrofitCall = RetrofitClient.retrofitCall
        val createPostInfo = CreatePostBody(AuthManager.currentUser.id, title, text, tags)
        retrofitCall.createPost(createPostInfo).enqueue(object : Callback<network.Post> {
            override fun onFailure(call: Call<network.Post>, t: Throwable) {
                println("failure on creating post")
            }
            override fun onResponse(call: Call<network.Post>, response: Response<network.Post>) {
                if (response.code() == 201) {
                    if (file != null) {
                        response.body()?.let {
                            uploadFile(it.id, file)
                        }
                    }
                } else {
                    println("response but wrong code on creating post")
                }
                clearFields()
            }
        })
    }

    fun uploadFile(postId: Int, file: File) {
        val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        RetrofitClient.retrofitCall.uploadPostFile(postId, body).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.code() != 200) {
                    println("wrong code on uploading post file")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                println("failure uploading post file")
            }

        })
    }

    data class CreatePostBody(
        val authorId: Int,
        val title: String,
        val content: String,
        val tags: String
    )
}
