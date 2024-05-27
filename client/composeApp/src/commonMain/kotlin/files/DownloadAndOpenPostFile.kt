package files

import androidx.compose.runtime.Composable

@Composable
expect fun DownloadAndOpenPdfButton(postId: Int, content: @Composable () -> Unit)
