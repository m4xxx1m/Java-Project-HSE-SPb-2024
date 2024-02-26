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
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import model.User

@Composable
fun UserProfileCard(thisUser: Boolean, user: User) {
    val userProfile = user.getProfile()
    Card(
        modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth(),
        elevation = 6.dp
    ) {
        Column(Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.clip(RoundedCornerShape(7.dp)).clickable { }) {
                    Image(
                        Icons.Rounded.Person, contentDescription = "User profile image",
                        modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.Red)
                    )
                    Image(
                        Icons.Rounded.Edit, contentDescription = null,
                        modifier = Modifier.size(18.dp).align(Alignment.BottomEnd)
                    )
                }
                Spacer(Modifier.size(20.dp))
                Text(user.name, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
            OutlinedCard("Contacts") {
                Text(userProfile.contacts)
            }
            OutlinedCard("About me") {
                Text(userProfile.about)
            }
            SubscriptionsCard(userProfile.subscriptions)
            OutlinedCard("Tags") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val tagsDragState = rememberLazyListState()
                    val tagsDragCoroutineScope = rememberCoroutineScope()
                    LazyRow(
                        state = tagsDragState,
                        modifier = Modifier.weight(1f)
                            .draggable(
                                orientation = Orientation.Horizontal,
                                state = rememberDraggableState { delta ->
                                    tagsDragCoroutineScope.launch {
                                        tagsDragState.scrollBy(-delta)
                                    }
                                })
                    ) {
                        items(userProfile.tags) { tag ->
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
                    if (thisUser) {
                        IconButton(onClick = { }) {
                            Image(
                                Icons.Rounded.Settings, contentDescription = "Edit tags",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }
            }
            OutlinedCard("") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("My CV", modifier = Modifier.weight(1f))
                    if (thisUser) {
                        IconButton(onClick = { }) {
                            Image(Icons.Rounded.Edit, contentDescription = null)
                        }
                        IconButton(onClick = { }) {
                            Image(Icons.Rounded.Add, contentDescription = null)
                        }
                    }
                    IconButton(onClick = { }) {
                        Image(Icons.Rounded.Info, contentDescription = null)
                    }
                }
            }
        }
    }
}
