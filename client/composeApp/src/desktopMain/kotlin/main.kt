import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.Post
import java.time.LocalTime

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "KotlinProject") {
        App()
    }
}

@Preview
@Composable
fun preview() {
    Post(
        0,
        "User name",
        painterResource(""), // probably won't work
        LocalTime.now(),
        "Post title",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor....",
        emptyList(),
        1000,
        100
    )
}
