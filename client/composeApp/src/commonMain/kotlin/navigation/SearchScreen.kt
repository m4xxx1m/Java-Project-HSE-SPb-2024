package navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import files.AvatarsDownloader
import kotlinx.coroutines.launch
import model.Post
import model.User
import network.RetrofitClient
import platform_depended.Platform
import platform_depended.getPlatform
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.PostCard
import ui.SearchWidget

class SearchScreen(private var searchQuery: String) : Screen {

    @Composable
    override fun Content() {
        val refreshHelper = remember { mutableStateOf(RefreshSearchHelper()) }
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
            }
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
                        SearchWidget(navigator = null, default = searchQuery) {
                            searchQuery = it
                            refreshHelper.value.load()
                        }
                        Spacer(Modifier.height(15.dp))
                        if (refreshHelper.value.posts.isEmpty()) {
                            Text(
                                text = "По запросу ничего не найдено",
                                color = Color.LightGray
                            )
                        }
                    }
                    items(refreshHelper.value.posts) { post ->
                        post.user = refreshHelper.value.users[post.userId]
                        PostCard(
                            post,
                            afterDeletePost = {
                                coroutineScope.launch {
                                    refreshHelper.value.load()
                                }
                            },
                            profilePicture = AvatarsDownloader.ProfilePictures[post.userId]
                        )
                        Spacer(Modifier.size(10.dp))
                    }
                }
            }
        }
    }

    private inner class RefreshSearchHelper : Refreshable() {
        val posts = mutableStateListOf<Post>()
        val users = mutableStateMapOf<Int, User>()

        private fun getUsersList(userIds: Set<Int>) {
            RetrofitClient.retrofitCall.getUsersList(userIds)
                .enqueue(object : Callback<List<network.User>> {
                    override fun onResponse(
                        call: Call<List<network.User>>,
                        response: Response<List<network.User>>
                    ) {
                        response.body()?.let {
                            it.forEach { user ->
                                users[user.userId] = user.convertUser()
                                if (!AvatarsDownloader.ProfilePictures.containsKey(user.userId)) {
                                    AvatarsDownloader.downloadProfilePicture(user.userId)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<network.User>>, t: Throwable) {
                        println("get users list failure")
                    }

                })
        }

        private fun searchPosts() {
            RetrofitClient.retrofitCall.searchPosts(searchQuery)
                .enqueue(object : Callback<List<network.Post>> {
                    override fun onResponse(
                        call: Call<List<network.Post>>,
                        response: Response<List<network.Post>>
                    ) {
                        if (response.code() == 200) {
                            response.body()?.let {
                                posts.clear()
                                val userIds = mutableSetOf<Int>()
                                posts.addAll(it.map { post ->
                                    userIds.add(post.authorId)
                                    post.convertPost()
                                })
                                getUsersList(userIds)
                            }
                        } else if (response.code() == 404) {
                            posts.clear()
                        } else {
                            println("Wrong code on searching")
                        }
                        isRefreshing = false
                    }

                    override fun onFailure(call: Call<List<network.Post>>, t: Throwable) {
                        println("Failure while searching")
                        isRefreshing = false
                    }

                })

        }

        override fun load() {
            isRefreshing = true
            searchPosts()
        }
    }
}