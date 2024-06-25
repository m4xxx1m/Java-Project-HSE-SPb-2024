package navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ui.AppTheme

object RefreshButton {
    var onRefresh: (() -> Unit)? = null
        set(value) {
            field = value
            showButton?.value = value != null
        }

    private var showButton: MutableState<Boolean>? = null

    @Composable
    fun Content() {
        showButton = remember { mutableStateOf(onRefresh != null) }
        val enabled = remember { mutableStateOf(true) }
        LaunchedEffect(enabled.value) {
            if (enabled.value) {
                return@LaunchedEffect
            } else {
                delay(1000L)
                enabled.value = true
            }
        }
        if (showButton?.value == true) {
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
                Icon(
                    Icons.Rounded.Refresh, 
                    contentDescription = "refresh",
                    tint = AppTheme.black
                )
            }
        }
    }
}
