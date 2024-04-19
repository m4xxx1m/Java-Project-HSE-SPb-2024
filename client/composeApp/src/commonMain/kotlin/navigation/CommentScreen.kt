package navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import model.Comment
import model.Post
import model.User
import network.ApiInterface
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.CommentCard
import ui.PostCard

class CommentScreen(private val postId: Int) : Screen {
    @Composable
    override fun Content() {
        val refreshHelper = remember { mutableStateOf(RefreshCommentsHelper(postId)) }
        RefreshableContent(refreshHelper) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                refreshHelper.value.post.value?.let {
                    PostCard(it)
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(
                        top = 10.dp,
                        bottom = 10.dp
                    )
                ) {
                    items(refreshHelper.value.comments) { comment ->
                        comment.user = refreshHelper.value.users[comment.authorId]
                        CommentCard(comment)
                        Spacer(Modifier.size(10.dp))
                    }
                }
            }
        }
        BackButton(LocalNavigator.currentOrThrow)
    }
}

private class RefreshCommentsHelper(private val postId: Int) : Refreshable() {
    val post = mutableStateOf<Post?>(null)
    val comments = mutableStateListOf<Comment>()
    val users = mutableStateMapOf<Int, User>()

    override fun load() {
        val retrofitCall = RetrofitClient.retrofit.create(ApiInterface::class.java)

        retrofitCall.getComments(postId).enqueue(object : Callback<List<network.Comment>> {
            override fun onFailure(call: Call<List<network.Comment>>, t: Throwable) {
                println("refresh comments screen failure")
                isRefreshing = false
            }

            override fun onResponse(
                call: Call<List<network.Comment>>,
                response: Response<List<network.Comment>>
            ) {
                if (response.code() == 200) {
                    println("refresh comments screen success")
                    comments.clear()
                    val userIds = mutableSetOf<Int>()
                    response.body()?.let {
                        comments.addAll(it.reversed().map { comment ->
                            userIds.add(comment.authorId)
                            comment.convertComment()
                        })
                    }

                    retrofitCall.getPost(postId).enqueue(object : Callback<network.Post> {
                        override fun onFailure(call: Call<network.Post>, t: Throwable) {
                            println("refresh comments screen failure")
                        }

                        override fun onResponse(
                            call: Call<network.Post>,
                            response: Response<network.Post>
                        ) {
                            response.body()?.let {
                                post.value = it.convertPost()
                            }

                            retrofitCall.getUsersList(userIds)
                                .enqueue(object : Callback<List<network.User>> {
                                    override fun onFailure(
                                        call: Call<List<network.User>>,
                                        t: Throwable
                                    ) {
                                        println("get users list failure")
                                    }

                                    override fun onResponse(
                                        call: Call<List<network.User>>,
                                        response: Response<List<network.User>>
                                    ) {
                                        response.body()?.let {
                                            it.forEach { user ->
                                                users[user.userId] = user.convertUser()
                                            }
                                        }
                                    }
                                })
                        }
                    })
                } else {
                    println("refresh comments screen wrong code")
                }
                isRefreshing = false
            }
        })
    }
}
