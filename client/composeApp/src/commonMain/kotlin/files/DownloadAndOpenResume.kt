package files

import androidx.compose.runtime.Composable

@Composable
expect fun DownloadAndOpenPostFileButton(postId: Int, content: @Composable () -> Unit)

@Composable
expect fun DownloadAndOpenUserCvButton(userId: Int, content: @Composable () -> Unit)
