package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.notifications

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Notification
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.NotificationDao

class GetNotificationByTaskId(private val notificationDao: NotificationDao) {
    suspend operator fun invoke(taskId: Int): Notification? {
        return notificationDao.getNotificationByTaskId(taskId)
    }
}