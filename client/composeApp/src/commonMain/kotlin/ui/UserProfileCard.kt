package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import model.AuthManager
import model.SubscriberManager
import model.User
import model.UserProfile
import navigation.TagSelectionScreen
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun UserProfileCard(user: User, navigator: Navigator? = null) {
    val thisUser = user.id == AuthManager.currentUser.id
    val userProfile = remember { mutableStateOf<UserProfile?>(null) }
    val subscriptionManager =
        if (thisUser) null else remember { mutableStateOf(SubscriberManager(user.id)) }
    if (userProfile.value == null) {
        user.setProfile(userProfile)
    }
    userProfile.value?.let { profile ->
        Card(
            modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth(),
            elevation = 6.dp
        ) {
            Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.clip(RoundedCornerShape(7.dp)).clickable { }) {
                        Image(
                            Icons.Rounded.Person, contentDescription = "User profile image",
                            modifier = Modifier.size(65.dp).clip(CircleShape).background(Color.Red)
                        )
                        Image(
                            Icons.Rounded.Edit, contentDescription = null,
                            modifier = Modifier.size(15.dp).align(Alignment.BottomEnd)
                        )
                    }
                    Spacer(Modifier.size(20.dp))
                    Text(user.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                if (!thisUser) {
                    Button(
                        onClick = {
                            subscriptionManager?.value?.changeSubscription()
                        }
                    ) {
                        Text(
                            if (subscriptionManager?.value?.isSubscribed?.value == true)
                                "Unsubscribe" else "Subscribe"
                        )
                    }
                }
                if (thisUser) {
                    TextFieldCard("Contacts", profile.contacts) {
                        updateContacts(user.id, it)
                    }
                    TextFieldCard("About me", profile.about) {
                        updateBio(user.id, it)
                    }
                } else {
                    OutlinedCard("Contacts") {
                        SelectionContainer {
                            Text(profile.contacts)
                        }
                    }
                    OutlinedCard("About me") {
                        SelectionContainer {
                            Text(profile.about)
                        }
                    }
                }
                SubscriptionsCard(profile.subscriptions, thisUser)
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
                            items(profile.tags) { tag ->
                                Card(
                                    backgroundColor = Color.Gray
                                ) {
                                    Text(
                                        tag.tagName,
                                        color = Color.White,
                                        modifier = Modifier.padding(3.dp)
                                    )
                                }
                                Spacer(Modifier.width(5.dp))
                            }
                        }
                        if (thisUser) {
                            IconButton(onClick = {
                                navigator?.push(TagSelectionScreen())
                            }) {
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
}

private fun updateContacts(userId: Int, contacts: String) {
    RetrofitClient.retrofitCall.updateContacts(userId, contacts)
        .enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.code() != 200) {
                    println("Wrong code while updating contacts")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Failure while updating contacts")
            }
        })
}

private fun updateBio(userId: Int, bio: String) {
    RetrofitClient.retrofitCall.updateBio(userId, bio)
        .enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.code() != 200) {
                    println("Wrong code while updating bio")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Wrong code while updating bio")
            }
        })
}
