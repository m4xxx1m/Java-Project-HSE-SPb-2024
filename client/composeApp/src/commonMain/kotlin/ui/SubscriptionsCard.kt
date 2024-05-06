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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
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
import model.User
import navigation.ManageSubscriptionsScreen
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SubscriptionsCard(
    userId: Int,
    navigator: Navigator?,
    needToUpdate: MutableState<Boolean>,
    uiOption: Boolean = false
) {
    val thisUser = userId == AuthManager.currentUser.id
    val subscriptions = remember { mutableStateListOf<User>() }
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        getSubscriptions(userId, subscriptions)
        needToUpdate.value = false
    }
    if (needToUpdate.value) {
        coroutineScope.launch {
            getSubscriptions(userId, subscriptions)
            needToUpdate.value = false
        }
    }
    if (!subscriptions.isEmpty()) {
        card(uiOption) {
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                Icons.Rounded.Person, contentDescription = "User profile image",
                                modifier = Modifier.size(40.dp).clip(CircleShape)
                                    .background(Color.Red)
                            )
                            var name = it.name
                            if (it.name.length > 8) {
                                name = name.substring(0, 8) + "..."
                            }
                            Text(
                                name,
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
                if (thisUser) {
                    IconButton(onClick = {
                        navigator?.push(ManageSubscriptionsScreen(subscriptions))
                    }) {
                        Image(
                            Icons.Rounded.ArrowForward,
                            contentDescription = "Manage subscriptions",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun card(uiOption: Boolean, content: @Composable () -> Unit) {
    if (uiOption) {
        Card(
            modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth(),
            elevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 5.dp,
                    start = 10.dp,
                    end = 10.dp,
                    bottom = 10.dp
                ).fillMaxWidth()
            ) {
                Text(
                    "Subscriptions", fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
                Spacer(Modifier.size(10.dp))
                content()
            }
        }
    } else {
        OutlinedCard("Subscriptions", content)
    }
}

private fun getSubscriptions(userId: Int, subscriptions: MutableList<User>) {
    RetrofitClient.retrofitCall.getSubscriptions(userId)
        .enqueue(object : Callback<List<network.User>> {
            override fun onResponse(
                call: Call<List<network.User>>,
                response: Response<List<network.User>>
            ) {
                if (response.code() == 200) {
                    response.body()?.let {
                        subscriptions.clear()
                        subscriptions.addAll(it.map(network.User::convertUser))
                    }
                } else {
                    println("wrong code on getting list of subscriptions")
                }
            }

            override fun onFailure(call: Call<List<network.User>>, t: Throwable) {
                println("failure on getting list of subscriptions")
            }
        })
}
