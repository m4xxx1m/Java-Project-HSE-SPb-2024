
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import navigation.ConnectionErrorScreen
import ui.AppTheme

@Composable
fun App() {
    AppTheme.apply {
        Navigator(screen = ConnectionErrorScreen())
    }
}
