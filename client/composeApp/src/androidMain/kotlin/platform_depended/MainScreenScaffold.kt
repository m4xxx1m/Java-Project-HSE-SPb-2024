package platform_depended

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import navigation.CreatePostTab
import navigation.HomeTab
import navigation.NotificationsTab
import navigation.ProfileTab
import navigation.SubscriptionsTab
import ui.AppTheme
import ui.SearchWidget

@Composable
actual fun MainScreenScaffold(content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = {
            SearchWidget(LocalNavigator.current?.parent)
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.White,
                contentColor = AppTheme.black,
                elevation = 15.dp
            ) {
                TabItem(HomeTab)
                TabItem(NotificationsTab)
                TabItem(CreatePostTab)
                TabItem(SubscriptionsTab)
                TabItem(ProfileTab)
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
