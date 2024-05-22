package files

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun ProfilePictureUploader(
    thisUser: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    if (thisUser) {
        val pickImage = remember { mutableStateOf(false) }
        val selectedFile = remember { mutableStateOf<File?>(null) }

        Box(
            modifier = Modifier.clip(RoundedCornerShape(7.dp)).clickable {
                pickImage.value = true
            }
        ) {
            content()

            if (pickImage.value) {
                ImagePicker() {
                    pickImage.value = false
                    selectedFile.value = it
                }
            }
        }

        selectedFile.value?.let { file ->
            selectedFile.value = null
            uploadProfilePicture(file)
        }
    } else {
        Box() {
            content()
        }
    }
}
