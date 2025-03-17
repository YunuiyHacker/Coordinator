package yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.notification

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import okhttp3.internal.notify
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.Constants.NOTIFICATION_CHANNEL_DESCRIPTION
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.Constants.NOTIFICATION_CHANNEL_ID
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.Constants.NOTIFICATION_ID

class NotificationUtil(private val application: Application) {

    fun createMessage(
        title: String,
        content: String
    ): NotificationCompat.Builder {
        createNotificationChannel()

        return NotificationCompat.Builder(application, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_DESCRIPTION,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(false)
        notificationChannel.enableVibration(false)

        val notificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}