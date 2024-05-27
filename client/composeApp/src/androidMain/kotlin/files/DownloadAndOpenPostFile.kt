package files

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.RetrofitClient
import okhttp3.ResponseBody
import java.io.File

@Composable
actual fun DownloadAndOpenPdfButton(postId: Int, content: @Composable () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    Box(
        modifier = Modifier.clickable {
            scope.launch(Dispatchers.IO) {
                try {
                    val response: ResponseBody =
                        RetrofitClient.retrofitCall.getPostFile(postId)
                            .execute().body()!!
                    val file = File(context.cacheDir, "downloaded_file.pdf")
                    savePostFile(response, file)
                    withContext(Dispatchers.Main) {
                        openPdfFile(context, file)
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

fun openPdfFile(context: Context, file: File) {
    val uri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", file
    )

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    context.startActivity(intent)
}
