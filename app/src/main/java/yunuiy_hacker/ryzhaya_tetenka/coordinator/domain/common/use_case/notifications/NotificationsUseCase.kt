package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.notifications

data class NotificationsUseCase(
    val deleteNotificationOperator: DeleteNotificationOperator,
    val getNotificationByIdOperator: GetNotificationByIdOperator,
    val getNotificationByTagOperator: GetNotificationByTagOperator,
    val getNotificationsOperator: GetNotificationsOperator,
    val getNotificationByTaskId: GetNotificationByTaskId,
    val insertNotificationOperator: InsertNotificationOperator,
    val insertNotificationsOperator: InsertNotificationsOperator,
    val updateNotificationOperator: UpdateNotificationOperator
)
