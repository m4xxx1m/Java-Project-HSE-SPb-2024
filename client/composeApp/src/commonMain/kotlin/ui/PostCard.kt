package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import files.DownloadAndOpenPostFileButton
import kotlinx.coroutines.launch
import model.AuthManager
import model.Post
import navigation.CommentScreen
import navigation.UserProfileScreen
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter

@Composable
fun PostCard(
    post: Post,
    navigator: Navigator? = LocalNavigator.current,
    isInCommentsScreen: Boolean = false,
    afterDeletePost: (() -> Unit)? = null,
    profilePicture: ImageBitmap? = null
) {
    val thisUser = post.userId == AuthManager.currentUser.id
    val ratingState = remember { mutableStateOf(post.likesCount) }
    Card(
        modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth(),
        elevation = 0.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            val expanded = remember { mutableStateOf(false) }
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                IconButton(onClick = { expanded.value = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Show dropdown menu",
                        Modifier.size(23.dp),
                        tint = AppTheme.black
                    )
                }
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = {
                        expanded.value = false
                    }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            savePost(post.id)
                            expanded.value = false
                        }
                    ) {
                        Text("Сохранить/удалить из сохраненных")
                    }
                    if (thisUser) {
                        DropdownMenuItem(
                            onClick = {
                                expanded.value = false
                                deletePost(post.id, afterDeletePost)
                            }
                        ) {
                            Text("Удалить пост")
                        }
                    }
                }
            }
            Column {
                Row {
                    if (profilePicture == null) {
                        Image(
                            Icons.Rounded.Person,
                            contentDescription = "User Image",
                            colorFilter = ColorFilter.tint(Color(0xfff0f2f5)),
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.primaryVariant)
                                .clickable {
                                    post.user?.let {
                                        navigator?.push(UserProfileScreen(it))
                                    }
                                }
                        )
                    } else {
                        Image(
                            bitmap = profilePicture,
                            contentDescription = "User Image",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    post.user?.let {
                                        navigator?.push(UserProfileScreen(it))
                                    }
                                }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = post.user?.name ?: "",
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = post.dateTime.format(
                                DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
                            ),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                Spacer(Modifier.height(7.dp))
                val tagsDragState = rememberLazyListState()
                val tagsDragCoroutineScope = rememberCoroutineScope()
                if (post.tags.isNotEmpty()) {
                    LazyRow(
                        state = tagsDragState,
                        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()
                            .draggable(
                                orientation = Orientation.Horizontal,
                                state = rememberDraggableState { delta ->
                                    tagsDragCoroutineScope.launch {
                                        tagsDragState.scrollBy(-delta)
                                    }
                                })
                    ) {
                        items(post.tags) { tag ->
                            Card(
                                elevation = 0.dp,
                                backgroundColor = MaterialTheme.colors.secondaryVariant
                            ) {
                                Text(
                                    tag,
                                    color = Color.White,
                                    modifier = Modifier.padding(3.dp)
                                )
                            }
                            Spacer(Modifier.width(5.dp))
                        }
                    }
                }
                if (post.title.isNotEmpty()) {
                    Text(
                        post.title,
                        style = MaterialTheme.typography.h6,
                        color = AppTheme.black
                    )
                }
                if (post.text.isNotEmpty()) {
                    Text(post.text, color = AppTheme.black)
                }

                if (post.fileName != null) {
                    Spacer(Modifier.size(5.dp))
                    DownloadAndOpenPostFileButton(post.id) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(7.dp))
                                .background(MaterialTheme.colors.background)
                                .padding(horizontal = 6.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Rounded.AttachFile,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(MaterialTheme.colors.primaryVariant)
                                    .padding(3.dp),
                                tint = AppTheme.black
                            )
                            Spacer(Modifier.size(7.dp))
                            Text(post.fileName, color = AppTheme.black)
                        }
                    }
                }

//                Box(Modifier
//                    .fillMaxWidth()
//                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
//                ) {
//                    Divider(Modifier.fillMaxWidth(), color = Color.LightGray, thickness = 0.4.dp)
//                }
                Spacer(Modifier.size(5.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(Modifier.clip(CircleShape).clickable {
                        likePost(post.id, ratingState)
                    }.padding(5.dp)) {
                        Icon(
                            Icons.Rounded.KeyboardArrowUp,
                            contentDescription = "Upvote post",
                            modifier = Modifier.size(25.dp),
                            tint = AppTheme.black
                        )
                    }
                    Text(ratingState.value.toString(), color = AppTheme.black, fontSize = 12.sp)
                    Box(Modifier.clip(CircleShape).clickable {
                        dislikePost(post.id, ratingState)
                    }.padding(5.dp)) {
                        Icon(
                            Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "Downvote post",
                            modifier = Modifier.size(25.dp),
                            tint = AppTheme.black
                        )
                    }

                    if (!isInCommentsScreen) {
                        Spacer(Modifier.width(5.dp))
                        Box(Modifier.clip(CircleShape).clickable {
                            navigator?.push(CommentScreen(post.id))
                        }.padding(9.dp)) {
                            Icon(
                                Icons.Rounded.Forum,
                                contentDescription = "Comments",
                                modifier = Modifier.size(21.dp),
                                tint = AppTheme.black
                            )
                        }
                        Text(
                            post.commentsCount.toString(), color = AppTheme.black,
                            fontSize = 12.sp
                        )
                    }

                }
            }
        }
    }
}

private fun deletePost(postId: Int, afterDeletingPost: (() -> Unit)?) {
    RetrofitClient.retrofitCall.deletePost(postId).enqueue(object : Callback<Unit> {
        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            if (response.code() == 200) {
                afterDeletingPost?.invoke()
            } else {
                println("wrong code on deleting post")
            }
        }

        override fun onFailure(call: Call<Unit>, t: Throwable) {
            println("failure on deleting post")
        }

    })
}

private fun savePost(postId: Int) {
    RetrofitClient.retrofitCall.savePost(postId, AuthManager.currentUser.id).enqueue(
        object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.code() != 200) {
                    println("wrong code on saving post")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                println("failure on saving post")
            }
        }
    )
}

private fun likePost(postId: Int, rating: MutableState<Int>) {
    val userId = AuthManager.currentUser.id
    val retrofitCall = RetrofitClient.retrofitCall
    retrofitCall.likePost(postId, userId).enqueue(object : Callback<Int> {
        override fun onResponse(call: Call<Int>, response: Response<Int>) {
            response.body()?.let {
                rating.value = it
            }
        }

        override fun onFailure(call: Call<Int>, t: Throwable) {}
    })
}

private fun dislikePost(postId: Int, rating: MutableState<Int>) {
    val userId = AuthManager.currentUser.id
    val retrofitCall = RetrofitClient.retrofitCall
    retrofitCall.dislikePost(postId, userId).enqueue(object : Callback<Int> {
        override fun onResponse(call: Call<Int>, response: Response<Int>) {
            response.body()?.let {
                rating.value = it
            }
        }

        override fun onFailure(call: Call<Int>, t: Throwable) {}
    })
}
