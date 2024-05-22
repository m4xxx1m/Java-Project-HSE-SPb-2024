package files

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import network.RetrofitClient
import okhttp3.ResponseBody
import org.jetbrains.skia.Image
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AvatarsDownloader {
    val ProfilePictures = mutableMapOf<Int, ImageBitmap?>()
    
    fun downloadProfilePicture(userId: Int) {
        if (ProfilePictures.containsKey(userId)) {
            return
        }
        RetrofitClient.retrofitCall.getUserProfilePicture(userId).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.code() == 200) {
                        response.body()?.bytes()?.let {
                            if (it.isNotEmpty()) {
                                val bitmap =
                                    Image.makeFromEncoded(it).toComposeImageBitmap()
                                ProfilePictures[userId] = bitmap
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("error on downloading profile picture")
                }

            }
        )
    }
}
