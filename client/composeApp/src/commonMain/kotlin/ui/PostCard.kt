package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.Post

@Composable
fun PostCard(post: Post) {
    Card(
        modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth(),
        elevation = 6.dp
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            var expanded by remember { mutableStateOf(false) }
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Show dropdown menu")
                }
            }
            Column {
                Row {
                    Image(
                        Icons.Rounded.Person,
                        contentDescription = "User Image",
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Red)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = post.user?.name ?: "")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = post.dateTime.toString())
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
                                backgroundColor = Color.Gray
                            ) {
                                Text(
                                    tag.name,
                                    color = Color.White,
                                    modifier = Modifier.padding(3.dp)
                                )
                            }
                            Spacer(Modifier.width(5.dp))
                        }
                    }
                }
                if (post.title.isNotEmpty()) {
                    Text(post.title, style = MaterialTheme.typography.h6)
                }
                Text(post.text)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {}) {
                        Image(Icons.Rounded.KeyboardArrowUp, contentDescription = "Upvote post")
                    }
                    Text(post.likesCount.toString())
                    IconButton(onClick = {}) {
                        Image(Icons.Rounded.KeyboardArrowDown, contentDescription = "Downvote post")
                    }
                    Spacer(Modifier.width(5.dp))
                    IconButton(onClick = {}) {
                        Image(Icons.Rounded.Email, contentDescription = "Comments")
                    }
                    Text(post.commentsCount.toString())
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = {}) {
                        Image(Icons.Rounded.Share, contentDescription = "Share")
                    }
                }
            }
        }
    }
}
