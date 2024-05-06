package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import model.AuthManager
import model.Comment
import navigation.UserProfileScreen
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter

@Composable
fun CommentCard(comment: Comment, isAnswerToAnswer: Boolean = false) {
    val rating = remember { mutableStateOf(comment.likesCount) }
    val navigator = LocalNavigator.current
    Card(elevation = 6.dp, modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Spacer(Modifier.width(35.dp))
            Image(
                Icons.Rounded.Person, 
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .clickable {
                        comment.user?.let {
                            navigator?.push(UserProfileScreen(it))
                        }
                    }
            )
            Spacer(Modifier.size(10.dp))
            Column {
                Row {
                    Column(Modifier.weight(1f)) {
                        Text(
                            comment.user?.name ?: "",
                            fontWeight = FontWeight.SemiBold
                        )
                        SelectionContainer {
                            Text(comment.text, fontSize = 14.sp)
                        }
                        SelectionContainer {  }
                    }
                    Column {
                        Image(
                            Icons.Rounded.MoreVert,
                            contentDescription = "Show dropdown menu",
                            modifier = Modifier.size(31.dp).clip(CircleShape).clickable { }
                                .padding(3.dp)
                        )
                        if (isAnswerToAnswer) {
                            Image(
                                Icons.Rounded.KeyboardArrowUp,
                                contentDescription = "To previous comment",
                                modifier = Modifier.size(31.dp).clip(CircleShape).clickable { }
                                    .padding(3.dp)
                            )
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = comment.dateTime.format(
                            DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")),
                        color = Color.Gray, 
                        fontSize = 11.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Image(Icons.Rounded.MailOutline, contentDescription = "Answer",
                        modifier = Modifier.size(31.dp).padding(3.dp).clip(CircleShape)
                            .clickable { }
                    )
                    Image(
                        Icons.Rounded.KeyboardArrowUp,
                        contentDescription = "Upvote comment",
                        modifier = Modifier.size(31.dp).padding(3.dp).clip(CircleShape)
                            .clickable {
                                likeComment(comment, rating)
                            }
                    )
                    Text(rating.value.toString())
                    Image(
                        Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Downvote comment",
                        modifier = Modifier.size(31.dp).padding(3.dp).clip(CircleShape)
                            .clickable {
                                dislikeComment(comment, rating)
                            }
                    )
                }
            }
        }
    }
}

private fun likeComment(comment: Comment, rating: MutableState<Int>) {
    val userId = AuthManager.currentUser.id
//    val retrofitCall = RetrofitClient.retrofit.create(ApiInterface::class.java)
    val retrofitCall = RetrofitClient.retrofitCall
    retrofitCall.likeComment(comment.postId, comment.id, userId).enqueue(object : Callback<Int> {
        override fun onResponse(call: Call<Int>, response: Response<Int>) {
            if (response.code() == 200) {
                response.body()?.let {
                    rating.value = it
                }
            }
        }

        override fun onFailure(call: Call<Int>, t: Throwable) {}
    })
}

private fun dislikeComment(comment: Comment, rating: MutableState<Int>) {
    val userId = AuthManager.currentUser.id
//    val retrofitCall = RetrofitClient.retrofit.create(ApiInterface::class.java)
    val retrofitCall = RetrofitClient.retrofitCall
    retrofitCall.dislikeComment(comment.postId, comment.id, userId).enqueue(object : Callback<Int> {
        override fun onResponse(call: Call<Int>, response: Response<Int>) {
            if (response.code() == 200) {
                response.body()?.let {
                    rating.value = it
                }
            }
        }

        override fun onFailure(call: Call<Int>, t: Throwable) {}
    })
}
