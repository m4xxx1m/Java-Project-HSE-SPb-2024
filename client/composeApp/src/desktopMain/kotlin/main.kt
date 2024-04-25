
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import model.Notification
import model.Post
import model.Tag
import model.User
import ui.ChooseInterestsForm
import ui.NewPostForm
import ui.NotificationWidget
import ui.PostCard
import ui.SearchWidget
import ui.SubscriptionsCard
import ui.UserProfileCard
import java.time.LocalDateTime

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication, 
        title = "Internfon"
    ) {
        App()
    }
}

//@Preview
//@Composable
//fun previewSignInScreen() {
//    SignInForm()
//}

@Preview
@Composable
fun previewApp() {
    App()
}

//@Preview
//@Composable
//fun previewSignUpEmailScreen() {
//    SignUpEmailForm()
//}

//@Preview
//@Composable
//fun previewSignUpUserDataScreen() {
//    SignUpUserDataForm()
//}

@Preview
@Composable
fun previewChooseInterestsScreen() {
    val tags = listOf(
        Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
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
fun previewPostCard() {
    Column(
        modifier = Modifier.background(Color.LightGray).padding(10.dp).fillMaxWidth()
//        modifier = Modifier.padding(10.dp).fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PostCard(
            Post(
                0,
                0,
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
fun previewNotificationWidget() {
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
                    0,
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
                    0,
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
fun previewNewPostForm() {
    NewPostForm()
}

@Preview
@Composable
fun previewSubscriptionsCard() {
    SubscriptionsCard(
        listOf(
            User(0, "User name", ""),
            User(0, "User name", ""),
            User(0, "User name", ""),
            User(0, "User name", ""),
            User(0, "User name", ""),
            User(0, "User name", "")
        )
    )
}

@Preview
@Composable
fun previewUserProfile() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.LightGray)
            .padding(10.dp)
    ) {
        UserProfileCard(User(0, "User name", ""))
    }
}

//@Preview
//@Composable
//fun previewComment() {
//    CommentCard(
//        Comment(
//            0,
//            User(0, "User name", ""),
//            LocalDateTime.MIN,
//            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
//            emptyList(),
//            1000,
//        ),
//        true
//    )
//}

@Preview
@Composable
fun previewSearchWidget() {
    SearchWidget()
}
