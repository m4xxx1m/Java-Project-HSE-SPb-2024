package files

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.io.File

@Composable
actual fun ImagePicker(
    onImageSelected: (File?) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val context = LocalContext.current as ComponentActivity
    val coroutineScope = rememberCoroutineScope()

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        onImageSelected(
            if (uri == null)
                null
            else
                cropAndCompressImage(uri, context)
        )
    }

    Box(
        modifier = Modifier.clip(RoundedCornerShape(7.dp)).clickable {
//            LaunchedEffect(pickMedia) {
                coroutineScope.launch {
                    pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                }
//            }
        }, content = content
    )
    
}

@Composable
actual fun PdfPicker(onPdfSelected: (File?) -> Unit, content: @Composable () -> Unit) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                val file = getFileFromUri(context, uri)
                onPdfSelected(file)
            }
        }
    }

    IconButton(onClick = {
        val intent = createPdfPickerIntent()
        launcher.launch(intent)
    }) {
        content()
    }
}

fun createPdfPickerIntent(): Intent {
    return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf"
    }
}

fun getFileFromUri(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, uri.lastPathSegment ?: "temp.pdf")
    inputStream?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return file
}
