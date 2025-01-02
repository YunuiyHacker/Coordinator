package yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.common.model

import java.util.Date

data class Task(
    val id: Int? = 0,
    val date: Date? = null,
    val title: String? = "",
    val content: String? = ""
)