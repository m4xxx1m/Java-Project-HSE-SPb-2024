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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Comment

@Composable
fun CommentCard(comment: Comment, isAnswerToAnswer: Boolean = false) {
    Card(elevation = 6.dp, modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Spacer(Modifier.width(35.dp))
            Image(Icons.Rounded.Person, contentDescription = null,
                modifier = Modifier.size(35.dp).clip(CircleShape).background(Color.Red)
                    .clickable { })
            Spacer(Modifier.size(10.dp))
            Column() {
                Row() {
                    Column(Modifier.weight(1f)) {
                        Text(comment.user?.name ?: "", fontSize = 18.sp)
                        Text(comment.text, fontSize = 14.sp)
                    }
                    Column() {
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
                LazyRow {

                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(comment.dateTime.toString(), color = Color.LightGray, fontSize = 11.sp)
                    Spacer(Modifier.weight(1f))
                    Image(Icons.Rounded.MailOutline, contentDescription = "Answer",
                        modifier = Modifier.size(31.dp).padding(3.dp).clip(CircleShape)
                            .clickable { }
                    )
                    Image(
                        Icons.Rounded.KeyboardArrowUp,
                        contentDescription = "Upvote comment",
                        modifier = Modifier.size(31.dp).padding(3.dp).clip(CircleShape)
                            .clickable { }
                    )
                    Text(comment.likesCount.toString())
                    Image(
                        Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Downvote comment",
                        modifier = Modifier.size(31.dp).padding(3.dp).clip(CircleShape)
                            .clickable { }
                    )
                }
            }
        }
    }
}