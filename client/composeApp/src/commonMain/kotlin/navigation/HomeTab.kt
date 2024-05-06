package navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import model.Post
import model.User
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.PostCard

object HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Rounded.Home)
            val title = "Home"
            val index: UShort = 0u
            return TabOptions(index, title, icon)
        }

    private var scrollState: Pair<Int, Int> = Pair(0, 0)

    @Composable
    override fun Content() {
        val lazyListState =
            rememberLazyListState(scrollState.first, scrollState.second)
        DisposableEffect(scrollState) {
            onDispose {
                scrollState = Pair(
                    lazyListState.firstVisibleItemIndex,
                    lazyListState.firstVisibleItemScrollOffset
                )
            }
        }
        val refreshHelper = remember { mutableStateOf(RefreshHomeHelper()) }
        RefreshableContent(refreshHelper) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(
                    top = 10.dp,
                    bottom = 10.dp
                ),
                state = lazyListState
            ) {
                items(refreshHelper.value.posts) { post ->
                    post.user = refreshHelper.value.users[post.userId]
                    PostCard(post)
                    Spacer(Modifier.size(10.dp))
                }
            }
        }
    }
}

private class RefreshHomeHelper : Refreshable() {
    val posts = mutableStateListOf<Post>()
    val users = mutableStateMapOf<Int, User>()

    override fun load() {
        val retrofitCall = RetrofitClient.retrofitCall
        retrofitCall.getAllPosts().enqueue(object : Callback<List<network.Post>> {
            override fun onFailure(call: Call<List<network.Post>>, t: Throwable) {
                println("refresh home tab failure")
                isRefreshing = false
            }

            override fun onResponse(
                call: Call<List<network.Post>>,
                response: Response<List<network.Post>>
            ) {
                if (response.code() == 200) {
                    println("refresh home tab success")
                    posts.clear()
                    val userIds = mutableSetOf<Int>()
                    response.body()?.let {
                        posts.addAll(it.map { post ->
                            userIds.add(post.authorId)
                            post.convertPost()
                        })
                    }
                    retrofitCall.getUsersList(userIds)
                        .enqueue(object : Callback<List<network.User>> {
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

                            override fun onFailure(call: Call<List<network.User>>, t: Throwable) {
                                println("get users list failure")
                            }

                        })
                } else {
                    println("refresh home tab wrong code")
                }
                isRefreshing = false
            }
        })
    }
}
