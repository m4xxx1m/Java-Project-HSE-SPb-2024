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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import files.AvatarsDownloader
import files.AvatarsDownloader.ProfilePictures
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.AuthManager
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
            val title = "Главный экран"
            val index: UShort = 0u
            return TabOptions(index, title, icon)
        }

    private var scrollState: Pair<Int, Int> = Pair(0, 0)
    private var posts = mutableStateListOf<Post>()
    private var users = mutableStateMapOf<Int, User>()
    private val initialized = mutableStateOf(false)

    @Composable
    override fun Content() {
        val lazyListState =
            rememberLazyListState(scrollState.first, scrollState.second)
        val reachedBottom = remember {
            derivedStateOf {
                val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index != 0 && lastVisibleItem?.index ==
                        lazyListState.layoutInfo.totalItemsCount - 1
            }
        }
        val reachedTop = remember {
            derivedStateOf {
                lazyListState.firstVisibleItemIndex == 0 &&
                        lazyListState.firstVisibleItemScrollOffset == 0
            }
        }

        val coroutineScope = rememberCoroutineScope()
        val refreshHelper =
            remember { mutableStateOf(RefreshHomeHelper(posts, users, coroutineScope)) }

        DisposableEffect(scrollState) {
            onDispose {
                scrollState = Pair(
                    lazyListState.firstVisibleItemIndex,
                    lazyListState.firstVisibleItemScrollOffset
                )
                posts = refreshHelper.value.posts
                users = refreshHelper.value.users
            }
        }

        LaunchedEffect(reachedTop.value) {
            coroutineScope.launch {
                while (reachedTop.value && initialized.value) {
                    refreshHelper.value.load()
                    delay(2000)
                }
            }
        }

        LaunchedEffect(reachedBottom.value)
        {
            if (reachedBottom.value) {
                refreshHelper.value.loadMore()
            }
        }

        RefreshableContent(refreshHelper, initialized)
        {
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
                    PostCard(
                        post,
                        navigator = LocalNavigator.current?.parent,
                        afterDeletePost = {
                            coroutineScope.launch {
                                refreshHelper.value.load()
                            }
                        },
                        profilePicture = ProfilePictures[post.userId]
                    )
                    Spacer(Modifier.size(10.dp))
                }
            }
        }
    }
}

private class RefreshHomeHelper(
    val posts: SnapshotStateList<Post> = mutableStateListOf(),
    val users: SnapshotStateMap<Int, User> = mutableStateMapOf(),
    val coroutineScope: CoroutineScope
) : Refreshable() {

    private fun getUsersList(userIds: Set<Int>) {
        RetrofitClient.retrofitCall.getUsersList(userIds)
            .enqueue(object : Callback<List<network.User>> {
                override fun onResponse(
                    call: Call<List<network.User>>,
                    response: Response<List<network.User>>
                ) {
                    if (response.code() == 200) {
                        coroutineScope.launch {
                            response.body()?.let {
                                it.forEach { user ->
                                    users[user.userId] = user.convertUser()
                                    if (!ProfilePictures.containsKey(user.userId)) {
                                        AvatarsDownloader.downloadProfilePicture(user.userId)
                                    }
                                }
                            }
                        }
                    } else {
                        println("wrong code on getting user list")
                    }
                }

                override fun onFailure(call: Call<List<network.User>>, t: Throwable) {
                    println("get users list failure")
                }

            })
    }

    private fun getAllPosts() {
        RetrofitClient.retrofitCall.getPostsForUser(AuthManager.currentUser.id)
            .enqueue(object : Callback<List<network.Post>> {
                override fun onFailure(call: Call<List<network.Post>>, t: Throwable) {
                    println("refresh home tab failure")
                    isRefreshing = false
                }

                override fun onResponse(
                    call: Call<List<network.Post>>,
                    response: Response<List<network.Post>>
                ) {
                    if (response.code() == 200) {
                        posts.clear()
                        val userIds = mutableSetOf<Int>()
                        response.body()?.let {
                            posts.addAll(it.map { post ->
                                userIds.add(post.authorId)
                                post.convertPost()
                            })
                        }
                        getUsersList(userIds)
                    } else {
                        println("refresh home tab wrong code")
                    }
                    isRefreshing = false
                }
            })
    }

    fun loadMore() {
        if (posts.isEmpty()) {
            return
        }
        RetrofitClient.retrofitCall.getMorePostsForUser(
            AuthManager.currentUser.id,
            posts.lastOrNull()?.id ?: -1
        ).enqueue(object : Callback<List<network.Post>> {
            override fun onResponse(
                call: Call<List<network.Post>>,
                response: Response<List<network.Post>>
            ) {
                if (response.code() == 200) {
                    val userIds = mutableSetOf<Int>()
                    response.body()?.let {
                        posts.addAll(it.map { post ->
                            userIds.add(post.authorId)
                            post.convertPost()
                        })
                        getUsersList(userIds)
                    }
                } else {
                    println("wrong code on getting more posts")
                }
            }

            override fun onFailure(call: Call<List<network.Post>>, t: Throwable) {
                println("error on getting more posts")
            }

        })
    }

    override fun load() {
//        isRefreshing = true
        getAllPosts()
    }
}
