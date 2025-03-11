package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model

import java.util.Date

data class Task(
    val id: Int = 0,
    val timeTypeId: Int = 0,
    val categoryId: Int = 0,
    val date: Date = Date(),
    val hour: Int = 0,
    val minute: Int = 0,
    val withEndTime: Boolean = false,
    val endHour: Int = 0,
    val endMinute: Int = 0,
    val title: String = "",
    val content: String = "",
    var checked: Boolean = false,
    var subtasks: List<Subtask> = listOf(),
    val notify: Boolean = false,
    val notifyDate: Date = Date(),
    val notifyHour: Int = 0,
    val notifyMinute: Int = 0
)