package files

import androidx.compose.ui.graphics.toComposeImageBitmap
import files.AvatarsDownloader.ProfilePictures
import model.AuthManager
import network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.jetbrains.skia.Image
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

fun uploadProfilePicture(file: File) {
    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
    val multipartBody = MultipartBody.Part.createFormData("profilePicture", file.name, requestBody)
    println(file.name)
    
    RetrofitClient.retrofitCall.uploadProfilePicture(
        AuthManager.currentUser.id,
        multipartBody
    ).enqueue(object : Callback<Unit> {
        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            if (response.code() == 200) {
                println("Image uploaded successfully")
                file.readBytes().let {
                    val bitmap = Image.makeFromEncoded(it).toComposeImageBitmap()
                    ProfilePictures[AuthManager.currentUser.id] = bitmap
                }
            } else {
                println("Image upload failed")
            }
            file.delete()
        }

        override fun onFailure(call: Call<Unit>, t: Throwable) {
            println("Error uploading image: ${t.message}")
            file.delete()
        }
    })
}
