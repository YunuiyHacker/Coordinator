package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.notification

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import okio.IOException
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.Constants.NOTIFICATION_ID
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.notification.NotificationUtil

class NotificationWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        val title = inputData.getString("title")
        val content = inputData.getString("content")

        return try {
            val notificationBuilder =
                NotificationUtil(applicationContext as Application).createMessage(
                    title.toString(), content.toString()
                )
            notificationBuilder.build()

            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID.toInt(), notificationBuilder.build())
            }

            Result.success()
        } catch (e: IOException) {
            Result.failure()
        }
    }
}