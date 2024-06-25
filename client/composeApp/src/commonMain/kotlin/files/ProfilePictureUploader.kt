package files

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.io.File

@Composable
fun ProfilePictureUploader(
    thisUser: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    if (thisUser) {
//        val pickImage = remember { mutableStateOf(false) }
        val selectedFile = remember { mutableStateOf<File?>(null) }
        
        ImagePicker(onImageSelected =  {
//            pickImage.value = false
            selectedFile.value = it
        }, content = content)

        selectedFile.value?.let { file ->
            selectedFile.value = null
            uploadProfilePicture(file)
        }
    } else {
        Box {
            content()
        }
    }
}
