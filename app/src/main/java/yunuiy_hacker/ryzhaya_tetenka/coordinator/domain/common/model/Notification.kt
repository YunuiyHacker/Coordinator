package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model

data class Notification(
    val id: Int = 0,
    val taskId: Int = 0,
    val tag: String = "",
    val isDone: Boolean = false
) {
}