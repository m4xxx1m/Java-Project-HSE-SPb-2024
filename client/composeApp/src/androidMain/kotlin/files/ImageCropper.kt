package files

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.math.min

private const val RESULT_SIZE = 300

fun cropAndCompressImage(imageFileUri: Uri, context: Context): File {
    val inputStream: InputStream? = context.contentResolver.openInputStream(imageFileUri)
    val bitmap = BitmapFactory.decodeStream(inputStream)

    val size = min(bitmap.height, bitmap.width)

    val croppedBitmap = Bitmap.createBitmap(
        size,
        size,
        bitmap.config
    )

    val canvas = Canvas(croppedBitmap)
    canvas.drawBitmap(
        bitmap,
        Rect(
            (bitmap.width - size) / 2,
            (bitmap.height - size) / 2,
            (bitmap.width - size) / 2 + size,
            (bitmap.height - size) / 2 + size
        ),
        Rect(0, 0, size, size),
        null
    )

    val resizedBitmap = Bitmap.createScaledBitmap(croppedBitmap, RESULT_SIZE, RESULT_SIZE, true)

    val file = File.createTempFile(
        imageFileUri.lastPathSegment ?: "temp", 
        ".png", 
        context.cacheDir
    )
    FileOutputStream(file).use {
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    inputStream?.close()
    return file
}
