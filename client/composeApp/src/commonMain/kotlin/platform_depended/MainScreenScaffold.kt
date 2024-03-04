package platform_depended

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

@Composable
expect fun MainScreenScaffold(content: @Composable (PaddingValues) -> Unit)
