package files

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

@Composable
private fun DownloadAndOpenPdfButton(
    call: Call<ResponseBody>,
    boxModifier: Modifier,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = boxModifier.clickable {
            scope.launch(Dispatchers.IO) {
                try {
                    val response: ResponseBody =
                        call.execute().body()!!
                    val file = File(context.cacheDir, "downloaded_file.pdf")
                    savePostFile(response, file)
                    withContext(Dispatchers.Main) {
                        openPdfFile(context, file)
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

private fun openPdfFile(context: Context, file: File) {
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
