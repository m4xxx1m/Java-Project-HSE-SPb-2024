package navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import platform_depended.MainScreenScaffold

class MainNavigation : Screen {
    @Composable
    override fun Content() {
        Column {
            TabNavigator(HomeTab) {
                MainScreenScaffold { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        CurrentTab()
                    }
                }
            }
        }
    }
}
