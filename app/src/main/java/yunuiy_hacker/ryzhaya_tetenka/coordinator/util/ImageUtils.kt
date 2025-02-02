package yunuiy_hacker.ryzhaya_tetenka.coordinator.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import java.io.File

object ImageUtils {
    val IMG_DIR =
        Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).absolutePath + "/Coordinator"

    fun getBitmapFromImagePath(absolutePathToImage: String): Bitmap? {
        val file = File(absolutePathToImage)
        if (file.exists()) return BitmapFactory.decodeFile(file.absolutePath)
        return null
    }
}