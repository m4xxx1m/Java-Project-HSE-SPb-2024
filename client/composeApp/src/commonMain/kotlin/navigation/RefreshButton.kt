package navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

object RefreshButton {
    var onRefresh: (() -> Unit)? = null

    @Composable
    fun Content() {
        val enabled = remember { mutableStateOf(true) }
        LaunchedEffect(enabled.value) {
            if (enabled.value) {
                return@LaunchedEffect
            } else {
                delay(1000L)
                enabled.value = true
            }
        }
        IconButton(
            modifier = Modifier.size(40.dp),
            onClick = {
                onRefresh?.let { 
                    it()
                }
                enabled.value = false
            },
            enabled = enabled.value
        ) {
            Image(Icons.Rounded.Refresh, contentDescription = "refresh")
        }
    }
}
