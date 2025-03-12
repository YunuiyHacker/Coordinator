package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.notifications

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Notification
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.NotificationDao

class UpdateNotificationOperator(private val notificationDao: NotificationDao) {
    suspend operator fun invoke(notification: Notification) {
        notificationDao.upsert(notification)
    }
}