package navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import files.AvatarsDownloader.ProfilePictures
import model.SubscriberManager
import model.User

class ManageSubscriptionsScreen(
    private val subscriptionList: List<User>
) : Screen {

    @Composable
    override fun Content() {
        val coroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.current
        Scaffold(
            topBar = {
                BackButton(navigator!!)
            }
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn(
                    modifier = Modifier
                        .widthIn(max = 500.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                    contentPadding = PaddingValues(
                        top = 10.dp,
                        bottom = 10.dp
                    )
                ) {
                    itemsIndexed(subscriptionList) { index, user ->
                        val subscriptionManager =
                            remember { mutableStateOf(SubscriberManager(user.id, coroutineScope)) }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ProfilePictures[user.id]?.let {
                                Image(
                                    bitmap = it,
                                    contentDescription = "User profile image",
                                    modifier = Modifier.size(40.dp).clip(CircleShape)
                                        .clickable { 
                                            navigator?.push(UserProfileScreen(user))
                                        }
                                )
                            } ?: run {
                                Image(
                                    Icons.Rounded.Person,
                                    contentDescription = "User profile image",
                                    colorFilter = ColorFilter.tint(Color(0xfff0f2f5)),
                                    modifier = Modifier.size(40.dp).clip(CircleShape)
                                        .background(MaterialTheme.colors.primaryVariant)
                                        .clickable { 
                                            navigator?.push(UserProfileScreen(user))
                                        }
                                )
                            }
                            Spacer(Modifier.size(15.dp))
                            Text(
                                text = user.name,
                                modifier = Modifier.weight(1f)
                            )
                            Button(onClick = {
                                subscriptionManager.value.changeSubscription()
                            }) {
                                Text(subscriptionManager.value.getButtonText())
                            }
                        }
                        Spacer(Modifier.size(7.dp))
                        if (index < subscriptionList.lastIndex) {
                            Box(Modifier.padding(horizontal = 8.dp)) {
                                Divider(color = Color.LightGray, thickness = 0.5.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}
