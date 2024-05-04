package navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import model.User
import ui.UserProfileCard

class UserProfileScreen(private val user: User) : Screen {
    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()
        Scaffold(
            topBar = {
                BackButton(LocalNavigator.currentOrThrow)
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(15.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                UserProfileCard(user, LocalNavigator.current)
            }
        }
    }
}
