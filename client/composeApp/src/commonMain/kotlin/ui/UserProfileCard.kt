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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import model.AuthManager
import model.Post
import model.SubscriberManager
import model.User
import model.UserProfile
import navigation.Refreshable
import navigation.RefreshableContent
import navigation.TagSelectionScreen
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileCard(private val user: User, private val navigator: Navigator?) {
    private var refreshHelper: MutableState<RefreshUserProfileHelper?> =
        mutableStateOf(null)

    enum class ShownPosts {
        USER_POSTS,
        SAVED_POSTS
    }

    @Composable
    fun Content() {
        val thisUser = user.id == AuthManager.currentUser.id
        refreshHelper = remember { mutableStateOf(RefreshUserProfileHelper(user)) }
        RefreshableContent(refreshHelper) {
            val shownPosts = remember { mutableStateOf(ShownPosts.USER_POSTS) }
            val coroutineScope = rememberCoroutineScope()
            refreshHelper.value?.let { helper ->
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        profileCard()
                    }
                    item {
                        if (helper.userProfile.value != null) {
                            if (thisUser) {
                                Row(
                                    modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(10.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (shownPosts.value == ShownPosts.USER_POSTS)
                                                    MaterialTheme.colors.primary
                                                else
                                                    MaterialTheme.colors.secondary
                                            ).clickable {
                                                shownPosts.value = ShownPosts.USER_POSTS
                                            }.padding(5.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "Посты " + user.name,
                                            color = if (shownPosts.value == ShownPosts.USER_POSTS)
                                                Color.White
                                            else
                                                Color.Black,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(10.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (shownPosts.value == ShownPosts.SAVED_POSTS)
                                                    MaterialTheme.colors.primary
                                                else
                                                    MaterialTheme.colors.secondary
                                            ).clickable {
                                                shownPosts.value = ShownPosts.SAVED_POSTS
                                            }.padding(5.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "Сохраненные посты",
                                            color = if (shownPosts.value == ShownPosts.SAVED_POSTS)
                                                Color.White
                                            else
                                                Color.Black,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                    }
                                }
                                Spacer(Modifier.size(10.dp))
                            } else {
                                Divider(
                                    modifier = Modifier
                                        .widthIn(max = 500.dp)
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                    color = Color.LightGray, 
                                    thickness = 0.5.dp
                                )
                                Spacer(Modifier.height(10.dp))
                            }
                        }
                    }
                    items(
                        if (shownPosts.value == ShownPosts.USER_POSTS)
                            helper.userPosts
                        else
                            helper.savedPosts
                    ) { post ->
                        post.user = helper.users[post.userId]
                        PostCard(
                            post,
                            afterDeletePost = {
                                coroutineScope.launch {
                                    helper.load()
                                }
                            }
                        )
                        Spacer(Modifier.size(10.dp))
                    }
                }
            }
        }
    }

    @Composable
    private fun profileCard() {
        val thisUser = user.id == AuthManager.currentUser.id
//        val userProfile = remember { mutableStateOf<UserProfile?>(null) }
        val coroutineScope = rememberCoroutineScope()
        val subscriptionManager =
            if (thisUser) null else remember {
                mutableStateOf(
                    SubscriberManager(
                        user.id,
                        coroutineScope
                    )
                )
            }
        val updateSubscriptionsList = remember { mutableStateOf(true) }
//        if (userProfile.value == null) {
//            coroutineScope.launch {
//                user.setProfile(userProfile)
//            }
//        }
        val isDialogOpened = remember { mutableStateOf(false) }
        if (thisUser && isDialogOpened.value) {
            AlertDialog(
                onDismissRequest = {
                    isDialogOpened.value = false
                },
                confirmButton = {
                    Button(onClick = {
                        navigator?.let {
                            val authManager = AuthManager()
                            var nav = it
                            while (nav.parent != null) {
                                nav = nav.parent!!
                            }
                            authManager.logOut(nav)
                        }
                    }) {
                        Text("Да")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = {
                        isDialogOpened.value = false
                    }) {
                        Text("Нет")
                    }
                },
                text = {
                    Text("Вы уверены, что хотите выйти из аккаунта?")
                }
            )
        }
        refreshHelper.value?.userProfile?.value?.let { profile ->
            Box(
                modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth(),
//            elevation = 6.dp
            ) {
                Column(
                    Modifier.padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.clip(RoundedCornerShape(7.dp)).clickable {}) {
                            Image(
                                Icons.Rounded.Person, 
                                contentDescription = "User profile image",
                                colorFilter = ColorFilter.tint(Color(0xfff0f2f5)),
                                modifier = Modifier.size(65.dp).clip(CircleShape)
                                    .background(MaterialTheme.colors.primaryVariant)
                            )
                            if (thisUser) {
                                Image(
                                    Icons.Rounded.Edit, contentDescription = null,
                                    modifier = Modifier.size(15.dp).align(Alignment.BottomEnd)
                                )
                            }
                        }
                        Spacer(Modifier.size(20.dp))
                        Text(
                            user.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        if (thisUser) {
                            IconButton(onClick = {
                                isDialogOpened.value = true
                            }) {
                                Image(Icons.Rounded.Logout, contentDescription = "Log out")
                            }
                        }
                    }
                    if (!thisUser) {
                        Button(
                            onClick = {
                                subscriptionManager?.value?.changeSubscription()
                            }
                        ) {
                            Text(
                                subscriptionManager?.value?.getButtonText() ?: "Подписаться"
                            )
                        }
                    }
                    if (thisUser) {
                        TextFieldCard("Контакты", profile.contacts) {
                            updateContacts(it)
                        }
                        TextFieldCard("О себе", profile.about) {
                            updateBio(it)
                        }
                    } else {
                        OutlinedCard("Контакты") {
                            SelectionContainer {
                                Text(profile.contacts)
                            }
                        }
                        OutlinedCard("О себе") {
                            SelectionContainer {
                                Text(profile.about)
                            }
                        }
                    }
                    SubscriptionsCard(user.id, navigator, updateSubscriptionsList)
                    OutlinedCard("Теги") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val tagsDragState = rememberLazyListState()
                            val tags = remember { mutableStateOf<ArrayList<String>?>(null) }
                            coroutineScope.launch {
                                profile.getTagsList(tags)
                            }
                            LazyRow(
                                state = tagsDragState,
                                modifier = Modifier.weight(1f)
                                    .draggable(
                                        orientation = Orientation.Horizontal,
                                        state = rememberDraggableState { delta ->
                                            coroutineScope.launch {
                                                tagsDragState.scrollBy(-delta)
                                            }
                                        })
                            ) {
                                items(tags.value ?: emptyList()) { tag ->
                                    Card(
                                        backgroundColor = MaterialTheme.colors.secondaryVariant
                                    ) {
                                        Text(
                                            tag,
                                            color = Color.White,
                                            modifier = Modifier.padding(3.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(5.dp))
                                }
                            }
                            if (thisUser) {
                                IconButton(onClick = {
                                    navigator?.push(TagSelectionScreen(profile.tags))
                                }) {
                                    Image(
                                        Icons.Rounded.Settings,
                                        contentDescription = "Edit tags",
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                            }
                        }
                    }
                    OutlinedCard("") {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Мое резюме", modifier = Modifier.weight(1f))
                            if (thisUser) {
                                IconButton(onClick = { }) {
                                    Image(Icons.Rounded.Edit, contentDescription = null)
                                }
                                IconButton(onClick = { }) {
                                    Image(Icons.Rounded.AttachFile, contentDescription = null)
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

    private fun updateContacts(contacts: String) {
        RetrofitClient.retrofitCall.updateContacts(user.id, contacts)
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

    private fun updateBio(bio: String) {
        RetrofitClient.retrofitCall.updateBio(user.id, bio)
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
}

private class RefreshUserProfileHelper(private val user: User) : Refreshable() {
    val userPosts = mutableStateListOf<Post>()
    val savedPosts = mutableStateListOf<Post>()
    val users = mutableStateMapOf<Int, User>()
    val userProfile = mutableStateOf<UserProfile?>(null)

    override fun load() {
        isRefreshing = true
        user.setProfile(userProfile)
        val retrofitCall = RetrofitClient.retrofitCall
        retrofitCall.getUserPosts(user.id).enqueue(object : Callback<List<network.Post>> {
            override fun onFailure(call: Call<List<network.Post>>, t: Throwable) {
                println("refresh user profile failure")
                isRefreshing = false
            }

            override fun onResponse(
                call: Call<List<network.Post>>,
                response: Response<List<network.Post>>
            ) {
                if (response.code() == 200) {
                    userPosts.clear()
                    val userIds = mutableSetOf<Int>()
                    response.body()?.let {
                        userPosts.addAll(it.map { post ->
                            userIds.add(post.authorId)
                            post.convertPost()
                        })
                    }
                    retrofitCall.getUsersList(userIds)
                        .enqueue(object : Callback<List<network.User>> {
                            override fun onResponse(
                                call: Call<List<network.User>>,
                                response: Response<List<network.User>>
                            ) {
                                response.body()?.let {
                                    it.forEach { user ->
                                        users[user.userId] = user.convertUser()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<List<network.User>>, t: Throwable) {
                                println("get users list failure")
                            }

                        })
                } else {
                    println("refresh user profile wrong code")
                }
                isRefreshing = false
            }
        })
        retrofitCall.getSavedPosts(user.id).enqueue(object : Callback<List<network.Post>> {
            override fun onFailure(call: Call<List<network.Post>>, t: Throwable) {
                println("refresh saved posts failure")
                isRefreshing = false
            }

            override fun onResponse(
                call: Call<List<network.Post>>,
                response: Response<List<network.Post>>
            ) {
                if (response.code() == 200) {
                    savedPosts.clear()
                    val userIds = mutableSetOf<Int>()
                    response.body()?.let {
                        savedPosts.addAll(it.map { post ->
                            userIds.add(post.authorId)
                            post.convertPost()
                        })
                    }
                    retrofitCall.getUsersList(userIds)
                        .enqueue(object : Callback<List<network.User>> {
                            override fun onResponse(
                                call: Call<List<network.User>>,
                                response: Response<List<network.User>>
                            ) {
                                response.body()?.let {
                                    it.forEach { user ->
                                        users[user.userId] = user.convertUser()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<List<network.User>>, t: Throwable) {
                                println("get users list failure")
                            }

                        })
                } else {
                    println("refresh saved posts wrong code")
                }
                isRefreshing = false
            }
        })
    }
}
