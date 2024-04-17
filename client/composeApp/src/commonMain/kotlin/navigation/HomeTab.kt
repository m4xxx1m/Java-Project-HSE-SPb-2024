package navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.Post
import model.User
import network.ApiInterface
import network.RetrofitClient
import platform_depended.Platform
import platform_depended.getPlatform
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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val refreshHelper = remember { mutableStateOf(RefreshHomeHelper()) }
        val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch {
            refreshHelper.value.load()
        }
        val onRefresh = {
            refreshHelper.value.isRefreshing = true
            coroutineScope.launch {
                refreshHelper.value.load()
            }
        }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = refreshHelper.value.isRefreshing,
            onRefresh = {
                onRefresh()
            }
        )
        Box(
            modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(
                    top = 10.dp,
                    bottom = 10.dp
                )
            ) {
                items(refreshHelper.value.list) { post ->
                    post.user = refreshHelper.value.users[post.userId]
                    PostCard(post)
                    Spacer(Modifier.size(10.dp))
                }
            }
            PullRefreshIndicator(
                refreshing = refreshHelper.value.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
        if (getPlatform() == Platform.DESKTOP) {
            val enabled = remember { mutableStateOf(true) }
            LaunchedEffect(enabled.value) {
                if (enabled.value) {
                    return@LaunchedEffect
                } else {
                    delay(1000L)
                    enabled.value = true
                }
            }
            Box(modifier = Modifier.padding(5.dp)) {
                IconButton(
                    modifier = Modifier.size(40.dp),
                    onClick = {
                        onRefresh()
                        enabled.value = false
                    },
                    enabled = enabled.value
                ) {
                    Image(Icons.Rounded.Refresh, contentDescription = "refresh")
                }
            }
        }
    }
}

private class RefreshHomeHelper {
    var isRefreshing = false
    val list = mutableStateListOf<Post>()
    val users = mutableStateMapOf<Int, User>()

    fun load() {
        val retrofitCall = RetrofitClient.retrofit.create(ApiInterface::class.java)
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
                    list.clear()
                    val userIds = mutableSetOf<Int>()
                    response.body()?.let {
                        list.addAll(it.reversed().map { post ->
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
