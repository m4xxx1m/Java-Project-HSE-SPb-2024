package files

import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun savePostFile(responseBody: ResponseBody, file: File) {
    var inputStream: InputStream? = null
    var outputStream: FileOutputStream? = null

    try {
        inputStream = responseBody.byteStream()
        outputStream = FileOutputStream(file)

        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        outputStream.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        inputStream?.close()
        outputStream?.close()
    }
}
