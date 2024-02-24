package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.User

@Composable
fun SubscriptionsCard(subscriptions: List<User>) {
    OutlinedCard("Subscriptions") {
        LazyRow(horizontalArrangement = Arrangement.SpaceBetween) {
            items(subscriptions) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(Icons.Rounded.Person, contentDescription = "User profile image",
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Red))
                    Text(it.name)
                }
            }
        }
    }
}
