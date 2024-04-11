package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.User

@Composable
fun SubscriptionsCard(subscriptions: List<User>) {
    OutlinedCard("Subscriptions") {
        val tagsDragState = rememberLazyListState()
        val tagsDragCoroutineScope = rememberCoroutineScope()
        Row(verticalAlignment = Alignment.CenterVertically) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                state = tagsDragState,
                modifier = Modifier.draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        tagsDragCoroutineScope.launch {
                            tagsDragState.scrollBy(-delta)
                        }
                    }).weight(1f)
            ) {
                items(subscriptions) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            Icons.Rounded.Person, contentDescription = "User profile image",
                            modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Red)
                        )
                        Text(it.name)
                    }
                }
            }
            IconButton(onClick = {}) {
                Image(
                    Icons.Rounded.ArrowForward, contentDescription = "Manage subscriptions",
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}
