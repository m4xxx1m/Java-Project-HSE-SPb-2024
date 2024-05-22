package files

import androidx.compose.runtime.Composable
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
