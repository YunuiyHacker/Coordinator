package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.ImageUtils
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.getHashedRandomString
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths

class SaveImageUseCase() {
    suspend operator fun invoke(bitmap: Bitmap): String {
        val newFileName =
            getHashedRandomString() + ".jpeg"
        val file =
            File(ImageUtils.IMG_DIR, newFileName)
        if (Files.notExists(Paths.get(ImageUtils.IMG_DIR)))
            withContext(Dispatchers.IO) {
                Files.createDirectory(Paths.get(ImageUtils.IMG_DIR))
            }
        val fos: FileOutputStream =
            withContext(Dispatchers.IO) {
                FileOutputStream(file)
            }
        withContext(Dispatchers.IO) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fos)
            fos.flush()
            fos.close()
        }

        return newFileName
    }
}
