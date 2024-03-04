package ru.hse.project

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import model.Notification
import model.Post
import model.Tag
import model.User
import ui.ChooseInterestsForm
import ui.NewPostForm
import ui.NotificationWidget
import ui.PostCard
import ui.SignUpUserDataForm
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                App()
            }
        }
    }
}

@Preview
@Composable
fun PreviewSignUpUserDataForm() {
    SignUpUserDataForm()
}

@Composable
fun PreviewChooseInterestsScreen() {
    val tags = listOf(Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
        Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
        Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
        Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
        Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
        Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm")
    )
    ChooseInterestsForm(tags)
}

@Preview
@Composable
fun PreviewPostCard() {
    Column(
        modifier = Modifier.background(Color.LightGray).padding(10.dp).fillMaxWidth()
//        modifier = Modifier.padding(10.dp).fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PostCard(
            Post(
                0,
                User(0, "User name", ""),
                LocalDateTime.MIN,
                listOf(Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm")),
                "Post title",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
                emptyList(),
                1000,
                100
            )
        )
    }
}

@Preview
@Composable
fun PreviewNotificationWidget() {
    Column(
        modifier = Modifier.background(Color.LightGray).padding(10.dp).fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NotificationWidget(
            Notification(
                0,
                Post(
                    0,
                    User(0, "User name", ""),
                    LocalDateTime.MIN,
                    emptyList(),
                    "Post title",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
                    emptyList(),
                    1000,
                    100
                ),
                Post(
                    0,
                    User(0, "User name", ""),
                    LocalDateTime.MIN,
                    emptyList(),
                    "Post title",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
                    emptyList(),
                    1000,
                    100
                )
            )
        )
    }
}

@Preview
@Composable
fun PreviewNewPostForm() {
    NewPostForm()
}
