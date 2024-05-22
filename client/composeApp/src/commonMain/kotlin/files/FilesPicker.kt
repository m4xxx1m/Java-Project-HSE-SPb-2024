package files

import androidx.compose.runtime.Composable
import java.io.File

@Composable
expect fun ImagePicker(onImageSelected: (File?) -> Unit)
