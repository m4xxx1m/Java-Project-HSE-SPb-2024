package navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import model.AuthManager
import ui.SignInScreen

class ConnectionErrorScreen : Screen {
    private var navigator: Navigator? = null

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        tryConnect()
        ConnectionErrorUi()
    }

    private fun tryConnect() {
        navigator?.let {
            val authManager = AuthManager()
            if (!authManager.tryLogin(it)) {
                it.replace(SignInScreen())
            }
        }
    }

    @Composable
    private fun ConnectionErrorUi() {
        val enabled = remember { mutableStateOf(true) }
        LaunchedEffect(enabled.value) {
            if (enabled.value) {
                return@LaunchedEffect
            } else {
                delay(1000L)
                enabled.value = true
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Couldn't connect to the server")
                Button(
                    onClick = {
                        enabled.value = false
                        tryConnect()
                    },
                    enabled = enabled.value
                ) {
                    Text("Try again")
                }
            }
        }
    }
}
