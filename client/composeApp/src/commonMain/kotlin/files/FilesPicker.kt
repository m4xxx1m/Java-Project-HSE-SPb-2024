package files

import androidx.compose.runtime.Composable
import java.io.File

@Composable
expect fun ImagePicker(onImageSelected: (File?) -> Unit)

@Composable
expect fun PdfPicker(onPdfSelected: (File?) -> Unit, content: @Composable () -> Unit)
