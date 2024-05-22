package files

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import java.io.File

//fun Uri.toFile(context: Context): File {
//    val inputStream: InputStream? = context.contentResolver.openInputStream(this)
//
//    val file = File(context.cacheDir, "temp")
//    FileOutputStream(file).use { output ->
//        inputStream?.copyTo(output)
//    }
//    inputStream?.close()
//    return file
//}

@Composable
actual fun ImagePicker(onImageSelected: (File?) -> Unit) {
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

    LaunchedEffect(pickMedia) {
        coroutineScope.launch {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
    }
}
