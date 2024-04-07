package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import network.ApiInterface
import network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun NewPostForm() {
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth()
                .fillMaxHeight().padding(10.dp)
        ) {
            val title = remember { mutableStateOf("") }
            val postText = remember { mutableStateOf("") }
            val postTags = remember { mutableListOf(0) }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title.value,
                onValueChange = { title.value = it },
                label = { Text("Post title", fontWeight = FontWeight.SemiBold) },
                maxLines = 3
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().weight(1f),
                value = postText.value,
                onValueChange = { postText.value = it },
                label = { Text("Post text") }
            )
            val checkedState = remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it },
                )
                Text("Attach CV from profile", fontWeight = FontWeight.Thin)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {}) {
                    Image(
                        Icons.Rounded.Add, contentDescription = "Attach images and files",
                        modifier = Modifier.size(35.dp)
                    )
                }
                Spacer(Modifier.weight(1f))
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                    modifier = Modifier.width(150.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.width(20.dp))
                        Spacer(Modifier.weight(1f))
                        Text("Tags")
                        Spacer(Modifier.weight(1f))
                        Image(Icons.Rounded.ArrowDropDown, contentDescription = "Edit post tags",
                            modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {
                    CreatePostManager().createPost(title.value, postText.value, postTags) {
                        
                    }
                }) {
                    Image(
                        Icons.Rounded.Done, contentDescription = "Post is done",
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
    }
}

class CreatePostManager {
    fun createPost(title: String, text: String, tags: List<Int>, onSuccess: () -> Unit) {
        val call = RetrofitClient.retrofit.create(ApiInterface::class.java)
        val createPostInfo = CreatePostBody(0, "$title\n$text", emptyList())
        call.createPost(createPostInfo).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("failure on creating post")
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 201) {
                    println("successfully created post")
                    onSuccess()
                } else {
                    println("response but wrong code on creating post")
                }
            }
        })
    }
    data class CreatePostBody(val authorId: Int, val content: String, val tagIds: List<Int>)
}
