import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import navigation.MainNavigation

@Composable
fun App() {
    MaterialTheme {
        Navigator(screen = MainNavigation())
    }
}
