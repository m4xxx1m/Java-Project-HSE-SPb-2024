package navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import model.AuthManager
import model.Comment
import model.Post
import model.User
import network.CommentCreate
import network.RetrofitClient
import platform_depended.Platform
import platform_depended.getPlatform
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.AppTheme
import ui.CommentCard
import ui.PostCard

class CommentScreen(private val postId: Int) : Screen {
    private var commentText: MutableState<String>? = null

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        commentText = remember { mutableStateOf("") }
        val refreshHelper = remember { mutableStateOf(RefreshCommentsHelper()) }
        val coroutineScope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                Row(Modifier.fillMaxWidth()) {
                    BackButton(LocalNavigator.currentOrThrow)
                    if (getPlatform() == Platform.DESKTOP) {
                        Spacer(Modifier.weight(1f))
                        RefreshButton.Content()
                    }
                }
            },
            bottomBar = {
                Box(
                    modifier = Modifier.fillMaxWidth().background(Color.White).padding(7.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier.widthIn(max = 500.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = commentText?.value ?: "",
                            onValueChange = {
                                commentText?.value = it
                            },
                            label = {
                                Text("Комментарий")
                            },
                            shape = RoundedCornerShape(10.dp)
                        )
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    refreshHelper.value.sendComment()
                                }
                            },
                            enabled = commentText?.value?.isNotEmpty() ?: false
                        ) {
                            Icon(Icons.Rounded.Send, contentDescription = "Send comment",
                                tint = AppTheme.black)
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                Modifier.padding(
                    bottom = innerPadding.calculateBottomPadding(),
                    top = innerPadding.calculateTopPadding()
                )
            ) {
                RefreshableContent(refreshHelper) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(
                            top = 10.dp,
                            bottom = 10.dp
                        )
                    ) {
                        item {
                            refreshHelper.value.post.value?.let { post ->
                                post.user = refreshHelper.value.users[post.userId]
                                PostCard(
                                    post,
                                    true,
                                    afterDeletePost = {
                                        navigator?.pop()
                                    })
                                Spacer(Modifier.size(15.dp))
                            }
                        }
                        itemsIndexed(refreshHelper.value.comments) { index, comment ->
                            comment.user = refreshHelper.value.users[comment.authorId]
                            CommentCard(
                                comment,
                                afterDeleteComment = {
                                    refreshHelper.value.load()
                                },
                                isFirstInList = index == 0,
                                isLastInList = index == refreshHelper.value.comments.size - 1
                            )
                        }
                    }
                }
            }
        }
    }

    inner class RefreshCommentsHelper : Refreshable() {
        val post = mutableStateOf<Post?>(null)
        val comments = mutableStateListOf<Comment>()
        val users = mutableStateMapOf<Int, User>()

        val retrofitCall = RetrofitClient.retrofitCall

        fun sendComment() {
            if (commentText == null) {
                return
            }
            retrofitCall.addComment(
                postId,
                CommentCreate(AuthManager.currentUser.id, commentText?.value ?: "")
            ).enqueue(object : Callback<network.Comment> {
                override fun onResponse(
                    call: Call<network.Comment>,
                    response: Response<network.Comment>
                ) {
                    if (response.code() == 200) {
                        commentText?.value = ""
                        load()
                    } else {
                        println("sending comment wrong code")
                    }
                }

                override fun onFailure(call: Call<network.Comment>, t: Throwable) {
                    println("sending comment failure")
                }
            })
        }

        override fun load() {
            isRefreshing = true
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
                            comments.addAll(it.map { comment ->
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
                                post.value?.let {
                                    userIds.add(it.userId)
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
}
