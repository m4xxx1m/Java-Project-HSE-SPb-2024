package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import model.Notification

@Composable
fun NotificationWidget(notification: Notification) {
    Card(
        modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth(),
        elevation = 6.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Image(
                Icons.Rounded.Person,
                contentDescription = "User image",
                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Red)
            )
            Spacer(Modifier.size(13.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(notification.answerPost.user.name, 
                    fontSize = TextUnit(16f, TextUnitType.Sp))
                Spacer(Modifier.size(3.dp))
                Column(
                    Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.LightGray)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("You", fontWeight = FontWeight.SemiBold, 
                        fontSize = TextUnit(14f, TextUnitType.Sp))
                    Text(notification.answerTo.title, maxLines = 1, 
                        fontSize = TextUnit(12f, TextUnitType.Sp))
                    Text(notification.answerTo.text, maxLines = 1,
                        fontSize = TextUnit(12f, TextUnitType.Sp))
                }
                Text(notification.answerPost.text, maxLines = 3,
                    fontSize = TextUnit(14f, TextUnitType.Sp))
                Text(notification.answerPost.dateTime.toString(),
                    color = Color.Gray,
                    fontSize = TextUnit(11f, TextUnitType.Sp)
                )
            }
            IconButton(onClick = {}) {
                Image(
                    Icons.Rounded.MoreVert, contentDescription = "Notification Menu"
                )
            }
        }
    }
}
