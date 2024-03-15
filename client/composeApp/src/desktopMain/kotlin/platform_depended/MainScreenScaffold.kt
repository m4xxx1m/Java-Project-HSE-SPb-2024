package platform_depended

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import navigation.CreatePostTab
import navigation.HomeTab
import navigation.NotificationsTab
import navigation.ProfileTab
import navigation.SubscriptionsTab
import ui.SearchWidget

@Composable
actual fun MainScreenScaffold(content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = {
            Row(Modifier.widthIn(min = 500.dp)) {
                BottomNavigation(
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    elevation = 0.dp,
                    modifier = Modifier.weight(3f)
                ) {
                    TabItem(HomeTab)
                    TabItem(NotificationsTab)
                    TabItem(CreatePostTab)
                    TabItem(SubscriptionsTab)
                    TabItem(ProfileTab)
                }
                Spacer(Modifier.weight(1f))
                Box(Modifier.weight(2f)) {
                    SearchWidget()
                }
            }
        },
        content = content
    )
}

@Composable
private fun RowScope.TabItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = {
            tabNavigator.current = tab
        },
        icon = {
            tab.options.icon?.let { painter ->
                Icon(painter, contentDescription = tab.options.title)
            }
        }
    )
}