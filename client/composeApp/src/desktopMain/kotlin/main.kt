import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Internfon",
        icon = BitmapPainter(useResource("icons/internfon_logo.png", ::loadImageBitmap))
    ) {
        App()
    }
}

//@Preview
//@Composable
//fun previewSignInScreen() {
//    SignInForm()
//}

//@Preview
//@Composable
//fun previewApp() {
//    App()
//}

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

//@Preview
//@Composable
//fun previewChooseInterestsScreen() {
//    val tags = listOf(
//        Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
//        Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
//        Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
//        Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
//        Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
//        Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm")
//    )
//    ChooseInterestsForm(tags)
//}

//@Preview
//@Composable
//fun previewPostCard() {
//    Column(
//        modifier = Modifier.background(Color.LightGray).padding(10.dp).fillMaxWidth()
////        modifier = Modifier.padding(10.dp).fillMaxWidth()
//            .fillMaxHeight(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        PostCard(
//            Post(
//                0,
//                0,
//                LocalDateTime.MIN,
//                listOf(
//                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
//                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
//                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
//                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
//                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
//                    Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm")
//                ),
//                "Post title",
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
//                emptyList(),
//                1000,
//                100
//            )
//        )
//    }
//}

//@Preview
//@Composable
//fun previewNotificationWidget() {
//    Column(
//        modifier = Modifier.background(Color.LightGray).padding(10.dp).fillMaxWidth()
//            .fillMaxHeight(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        NotificationWidget(
//            Notification(
//                0,
//                Comment(
//                    0,
//                    0,
//                    LocalDateTime.MIN,
//                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
//                    emptyList(),
//                    1000,
//                    100,
//                    0
//                ),
//                Comment(
//                    0,
//                    0,
//                    LocalDateTime.MIN,
//                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
//                    emptyList(),
//                    1000,
//                    100,
//                    -1
//                )
//            )
//        )
//    }
//}

//@Preview
//@Composable
//fun previewNewPostForm() {
//    NewPostForm()
//}

//@Preview
//@Composable
//fun previewSubscriptionsCard() {
//    SubscriptionsCard(
//        listOf(
//            User(0, "User name", ""),
//            User(0, "User name", ""),
//            User(0, "User name", ""),
//            User(0, "User name", ""),
//            User(0, "User name", ""),
//            User(0, "User name", "")
//        )
//    )
//}

//@Preview
//@Composable
//fun previewUserProfile() {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.LightGray)
//            .padding(10.dp)
//    ) {
//        UserProfileCard(User(0, "User name", ""))
//    }
//}

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

//@Preview
//@Composable
//fun previewSearchWidget() {
//    SearchWidget()
//}
