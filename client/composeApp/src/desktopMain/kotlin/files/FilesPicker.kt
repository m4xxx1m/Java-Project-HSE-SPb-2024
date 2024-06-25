package files

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun ImagePicker(
    onImageSelected: (File?) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
//    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier.clip(RoundedCornerShape(7.dp)).clickable {
//            coroutineScope.launch {
                val frame = Frame()
                val fileDialog = FileDialog(frame, "Choose an image", FileDialog.LOAD).apply {
                    file = "*.jpg;*.jpeg;*.png"
                    isVisible = true
                }

                val selectedFile = fileDialog.files?.firstOrNull()

                onImageSelected(
                    if (selectedFile == null)
                        null
                    else
                        cropAndCompressImage(selectedFile)
                )
//            }
        }
    ) {
        content()
    }
}

@Composable
actual fun PdfPicker(onPdfSelected: (File?) -> Unit, content: @Composable () -> Unit) {
//    val coroutineScope = rememberCoroutineScope()

    IconButton(onClick = {
        val file = pickPdfFile()
        if (file != null) {
//            coroutineScope.launch {
            onPdfSelected(file)
//            }
        }
    }) {
        content()
    }
}

fun pickPdfFile(): File? {
    val fileDialog = FileDialog(Frame(), "Select a PDF file", FileDialog.LOAD)
    fileDialog.file = "*.pdf"
    fileDialog.isVisible = true
    return fileDialog.files.firstOrNull()
}
