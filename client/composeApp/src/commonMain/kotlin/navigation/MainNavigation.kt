package navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import platform_depended.MainScreenScaffold

class MainNavigation : Screen {
    @Composable
    override fun Content() {
        Column {
            TabNavigator(HomeTab) {
                MainScreenScaffold {
                    CurrentTab()
                }
            }
        }
    }
}
