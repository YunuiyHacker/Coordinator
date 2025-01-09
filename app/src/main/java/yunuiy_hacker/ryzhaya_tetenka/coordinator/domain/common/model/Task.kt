package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
    var checked: MutableState<Boolean> = mutableStateOf(false)
)