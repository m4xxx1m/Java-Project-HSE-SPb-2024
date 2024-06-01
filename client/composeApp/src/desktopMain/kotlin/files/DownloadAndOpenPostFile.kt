package files

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import java.awt.Desktop
import java.io.File
import java.nio.file.Files

@Composable
private fun DownloadAndOpenPdfButton(
    call: Call<ResponseBody>,
    boxModifier: Modifier,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = boxModifier.clickable {
            scope.launch(Dispatchers.IO) {
                try {
                    val response: ResponseBody =
                        call.execute().body()!!
                    val file = getPdfFilePath()
                    savePostFile(response, file)
                    withContext(Dispatchers.Main) {
                        openPdfFile(file)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
actual fun DownloadAndOpenPostFileButton(postId: Int, content: @Composable () -> Unit) {
    DownloadAndOpenPdfButton(
        RetrofitClient.retrofitCall.getPostFile(postId),
        Modifier.clip(RoundedCornerShape(7.dp)),
        content
    )
}

@Composable
actual fun DownloadAndOpenUserCvButton(userId: Int, content: @Composable () -> Unit) {
    DownloadAndOpenPdfButton(
        RetrofitClient.retrofitCall.getUserResume(userId),
        Modifier.size(45.dp).clip(CircleShape),
        content
    )
}

private fun getPdfFilePath(): File {
    val tempFile = Files.createTempFile(null, ".pdf").toFile()
    tempFile.deleteOnExit()
    return tempFile
}

private fun openPdfFile(file: File) {
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().open(file)
    }
}
