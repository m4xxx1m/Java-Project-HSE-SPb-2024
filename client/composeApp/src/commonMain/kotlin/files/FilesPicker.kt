package files

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import java.io.File

@Composable
expect fun ImagePicker(
    onImageSelected: (File?) -> Unit, 
    content: @Composable BoxScope.() -> Unit
)

@Composable
expect fun PdfPicker(onPdfSelected: (File?) -> Unit, content: @Composable () -> Unit)
