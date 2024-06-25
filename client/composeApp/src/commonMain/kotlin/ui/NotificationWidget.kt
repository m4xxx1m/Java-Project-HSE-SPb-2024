package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import model.Notification
import navigation.CommentScreen
import navigation.UserProfileScreen
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationWidget(
    notification: Notification,
    navigator: Navigator? = null,
    replyAuthorProfilePicture: ImageBitmap? = null,
    afterDeleteNotification: (() -> Unit)? = null,
    isFirstInList: Boolean = false,
    isLastInList: Boolean = false
) {
    val shape = RoundedCornerShape(
        topStart = if (isFirstInList) 10.dp else 0.dp,
        topEnd = if (isFirstInList) 10.dp else 0.dp,
        bottomStart = if (isLastInList) 10.dp else 0.dp,
        bottomEnd = if (isLastInList) 10.dp else 0.dp
    )
    Column {
        Card(
            modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth(),
            shape = shape,
            elevation = 0.dp,
            onClick = {
                navigator?.push(CommentScreen(notification.replyComment.postId))
            }
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                    Spacer(Modifier.width(35.dp))
                    if (replyAuthorProfilePicture == null) {
                        Image(
                            Icons.Rounded.Person,
                            contentDescription = "User Image",
                            colorFilter = ColorFilter.tint(Color(0xfff0f2f5)),
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.primaryVariant)
                                .clickable {
                                    notification.replyComment.user?.let {
                                        navigator?.push(UserProfileScreen(it))
                                    }
                                }
                        )
                    } else {
                        Image(
                            bitmap = replyAuthorProfilePicture,
                            contentDescription = "User Image",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    notification.replyComment.user?.let {
                                        navigator?.push(UserProfileScreen(it))
                                    }
                                }
                        )
                    }
                    Spacer(Modifier.size(13.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            notification.replyComment.user?.name ?: "",
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.black
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max)
                                .clip(RoundedCornerShape(7.dp))
                                .background(MaterialTheme.colors.background)
                                .padding(6.dp)
                        ) {
                            Box(
                                Modifier
                                    .fillMaxHeight()
                                    .width(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(MaterialTheme.colors.primaryVariant)
                            )
                            Spacer(Modifier.width(5.dp))
                            Column {
                                Text(
                                    "Ваш комментарий", fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = AppTheme.black
                                )
                                Text(
                                    notification.originalComment.text, maxLines = 2,
                                    fontSize = 12.sp,
                                    color = AppTheme.black
                                )
                            }
                        }
                        Spacer(Modifier.height(3.dp))
                        Text(
                            notification.replyComment.text, maxLines = 3,
                            fontSize = 14.sp,
                            color = AppTheme.black
                        )
                        Spacer(Modifier.size(7.dp))
                        Text(
                            notification.replyComment.dateTime.format(
                                DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
                            ),
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                    val expanded = remember { mutableStateOf(false) }
                    Box {
                        Icon(
                            Icons.Rounded.MoreVert,
                            contentDescription = "Show dropdown menu",
                            modifier = Modifier.size(25.dp).clip(CircleShape)
                                .clickable {
                                    expanded.value = true
                                }.padding(3.dp),
                            tint = AppTheme.black
                        )
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = {
                                expanded.value = false
                            }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    deleteNotification(notification.id, afterDeleteNotification)
                                    expanded.value = false
                                }
                            ) {
                                Text("Удалить комментарий")
                            }
                        }
                    }
                }
            }
        }
        if (!isLastInList) {
            Box(modifier =  Modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
            ) {
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp, end = 20.dp),
                    color = Color.LightGray,
                    thickness = 0.5.dp
                )
            }
        }
    }
}

private fun deleteNotification(notificationId: Int, afterDeleteNotification: (() -> Unit)?) {
    RetrofitClient.retrofitCall.deleteNotification(notificationId).enqueue(object : Callback<Unit> {
        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            if (response.code() == 200) {
                afterDeleteNotification?.invoke()
            } else {
                println("wrong code on deleting notification")
            }
        }

        override fun onFailure(call: Call<Unit>, t: Throwable) {
            println("failure on deleting notification")
        }
    })
}
