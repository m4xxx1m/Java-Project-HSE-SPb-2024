package navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import files.AvatarsDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import model.AuthManager
import model.Notification
import model.User
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.NotificationWidget

object NotificationsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Rounded.Notifications)
            val title = "Уведомления"
            val index: UShort = 1u
            return TabOptions(index, title, icon)
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current?.parent
        val coroutineScope = rememberCoroutineScope()
        val refreshHelper = remember { mutableStateOf(RefreshNotificationsHelper(coroutineScope)) }
        RefreshableContent(refreshHelper) {
            if (refreshHelper.value.notifications.size > 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(
                        top = 10.dp,
                        bottom = 10.dp
                    )
                ) {
                    itemsIndexed(refreshHelper.value.notifications) { index, notification ->
                        notification.originalComment.user =
                            refreshHelper.value.users[notification.originalComment.authorId]
                        notification.replyComment.user =
                            refreshHelper.value.users[notification.replyComment.authorId]
                        NotificationWidget(
                            notification,
                            navigator,
                            AvatarsDownloader.ProfilePictures[notification.replyComment.authorId],
                            afterDeleteNotification = {
                                refreshHelper.value.load()
                            },
                            isFirstInList = index == 0,
                            isLastInList = index == refreshHelper.value.notifications.size - 1
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Новых уведомлений нет",
                        color = Color.Gray
                    )
                }
            }
        } 
    }
}

private class RefreshNotificationsHelper(val coroutineScope: CoroutineScope) : Refreshable() {
    val notifications = mutableStateListOf<Notification>()
    val users: SnapshotStateMap<Int, User> = mutableStateMapOf()

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
                                    if (!AvatarsDownloader.ProfilePictures.containsKey(user.userId)) {
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

    fun getNotifications() {
        RetrofitClient.retrofitCall.getNotificationsForUser(AuthManager.currentUser.id)
            .enqueue(object : Callback<List<network.Notification>> {
                override fun onResponse(
                    call: Call<List<network.Notification>>,
                    response: Response<List<network.Notification>>
                ) {
                    if (response.code() == 200) {
                        response.body()?.let {
                            notifications.clear()
                            val userIds = mutableSetOf<Int>()
                            notifications.addAll(it.map { notification ->
                                userIds.add(notification.originalComment.authorId)
                                userIds.add(notification.replyComment.authorId)
                                notification.convertNotification()
                            })
                            getUsersList(userIds)
                        }
                    } else {
                        println("wrong code on getting notifications")
                    }
                    isRefreshing = false
                }

                override fun onFailure(call: Call<List<network.Notification>>, t: Throwable) {
                    println("failure on getting notifications")
                    isRefreshing = false
                }
            })
    }

    override fun load() {
        isRefreshing = true
        getNotifications()
    }
}
