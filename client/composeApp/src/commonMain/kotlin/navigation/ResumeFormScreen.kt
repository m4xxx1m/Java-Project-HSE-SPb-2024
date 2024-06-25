package navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import platform_depended.Platform
import platform_depended.getPlatform
import ui.ResumeForm

class ResumeFormScreen : Screen {
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                Row(Modifier.fillMaxWidth()) {
                    BackButton(LocalNavigator.currentOrThrow)
                    if (getPlatform() == Platform.DESKTOP) {
                        Spacer(Modifier.weight(1f))
                        RefreshButton.Content()
                    }
                }
            }
        ) {
            Box(contentAlignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize()
            ) {
                ResumeForm()
            }
        }
    }
}
