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
import model.Tag
import ui.SignInScreen

class ConnectionErrorScreen : Screen {
    private var navigator: Navigator? = null
    private var onFailure: (() -> Unit)? = null

    @Composable
    override fun Content() {
        val isConnectionError = remember { mutableStateOf(false) }
        onFailure = {
            isConnectionError.value = true
        }
        navigator = LocalNavigator.currentOrThrow
        if (isConnectionError.value) {
            ConnectionErrorUi()
        }
        LaunchedEffect(Tag.Companion::class.java) {
            Tag.initTags()
        }
        tryConnect()
    }

    private fun tryConnect() {
        navigator?.let {
            val authManager = AuthManager()
            authManager.tryLogin(
                it,
                onFailure,
                onError = {
                    it.replace(SignInScreen())
                }
            )
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
                Text("Не удалось подключиться к серверу")
                Button(
                    onClick = {
                        enabled.value = false
                        tryConnect()
                    },
                    enabled = enabled.value
                ) {
                    Text("Попробовать снова")
                }
            }
        }
    }
}
