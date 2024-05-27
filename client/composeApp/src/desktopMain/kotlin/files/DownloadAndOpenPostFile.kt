package files

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.RetrofitClient
import okhttp3.ResponseBody
import java.awt.Desktop
import java.io.File
import java.nio.file.Files

@Composable
actual fun DownloadAndOpenPdfButton(postId: Int, content: @Composable () -> Unit) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.clickable {
            scope.launch(Dispatchers.IO) {
                try {
                    val response: ResponseBody =
                        RetrofitClient.retrofitCall.getPostFile(postId)
                            .execute().body()!!
                    val file = getPdfFilePath()
                    savePostFile(response, file)
                    withContext(Dispatchers.Main) {
                        openPdfFile(file)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    ) {
        content()
    }
}

fun getPdfFilePath(): File {
    val tempFile = Files.createTempFile(null, ".pdf").toFile()
    tempFile.deleteOnExit()
    return tempFile
}

fun openPdfFile(file: File) {
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().open(file)
    }
}
