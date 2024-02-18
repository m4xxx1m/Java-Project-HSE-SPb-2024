package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun Post(
    postId: Int,
    userName: String,
    userImage: Painter,
    dateTime: LocalTime,
    postTitle: String,
    postText: String,
    postImages: List<Painter>,
    likesCount: Int,
    commentsCount: Int
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Show dropdown menu")
            }
        }
    }
    Row(modifier = Modifier.padding(all = 8.dp)) { 
        Image(painter = userImage,
            contentDescription = "User avatar",
            modifier = Modifier.size(40.dp).clip(CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = userName)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = dateTime.toString())
        }
        
    }
}
