
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import model.Tag
import navigation.ConnectionErrorScreen
import ui.AppTheme

@Composable
fun App() {
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        Tag.initTags()
    }
    AppTheme.apply {
        Navigator(screen = ConnectionErrorScreen())
    }
}
