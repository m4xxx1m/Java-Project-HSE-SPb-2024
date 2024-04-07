package navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import model.Post
import network.ApiInterface
import network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.PostCard

object HomeTab: Tab {
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
        val pullRefreshState = rememberPullRefreshState(
            refreshing = refreshHelper.value.isRefreshing,
            onRefresh = refreshHelper.value::load
        )
        Box(
            modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(refreshHelper.value.list) { post ->
                    PostCard(post)
                }
            }
            PullRefreshIndicator(
                refreshing = refreshHelper.value.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

private class RefreshHomeHelper {
    val isRefreshing = false
    val list = mutableStateListOf<Post>()

    init {
        load()
    }

    fun load() {
        val call = RetrofitClient.retrofit.create(ApiInterface::class.java)
        call.getAllPosts().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("refresh home tab failure")
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    println("refresh home tab success")
                } else {
                    println("refresh home tab wrong code")
                }
            }
        })
    }
}
