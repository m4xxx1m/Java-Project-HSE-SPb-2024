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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import model.Post
import model.Tag
import model.User
import ui.PostCard
import ui.SignUpUserDataForm
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PreviewPostCard()
        }
    }
}


@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

@Preview
@Composable
fun PreviewAndroid() {
    SignUpUserDataForm()
}

@Preview
@Composable
fun PreviewPostCard() {
    Column(
        modifier = Modifier.background(Color.LightGray).padding(10.dp).fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PostCard(
            Post(
                0,
                User(0, "User name", ""),
                LocalDateTime.MIN,
                listOf(
                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm")
                ),
                "Post title",
                "Post text",
                emptyList(),
                1000,
                100
            )
        )
    }
}
