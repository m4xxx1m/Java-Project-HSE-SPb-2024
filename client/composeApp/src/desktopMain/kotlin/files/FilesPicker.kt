package files

import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun ImagePicker(onImageSelected: (File?) -> Unit) {

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
}

@Composable
actual fun PdfPicker(onPdfSelected: (File?) -> Unit, content: @Composable () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    
    IconButton(onClick = {
        val file = pickPdfFile()
        if (file != null) {
            coroutineScope.launch {
                onPdfSelected(file)
            }
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
