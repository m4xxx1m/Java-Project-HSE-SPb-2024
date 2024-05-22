package files

import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDateTime
import javax.imageio.ImageIO
import kotlin.math.min

fun cropAndCompressImage(imageFile: File): File {
    val bufferedImage = ImageIO.read(imageFile) ?: return imageFile

    val size = min(bufferedImage.width, bufferedImage.height)

    val croppedImage = bufferedImage.getSubimage(
        (bufferedImage.width - size) / 2,
        (bufferedImage.height - size) / 2,
        size, size
    )

    val scaledImage = scaleImage(croppedImage)

    val resultFile = File.createTempFile(getResultName(imageFile.name), ".png")
    ImageIO.write(scaledImage, "png", resultFile)
    resultFile.deleteOnExit()

    return resultFile
}

fun getResultName(fileName: String): String {
    val digest = MessageDigest.getInstance("MD5")
    digest.update((fileName + LocalDateTime.now()).toByteArray())
    return BigInteger(1, digest.digest()).toString(16)
}

fun scaleImage(image: BufferedImage): BufferedImage {
    val newSize = 300
    val resized = BufferedImage(newSize, newSize, image.type)

    val graphics = resized.createGraphics()
    graphics.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR
    )
    graphics.drawImage(image, 0, 0, newSize, newSize, 
        0, 0, image.width, image.height, null)
    graphics.dispose()
    
    return resized
}
