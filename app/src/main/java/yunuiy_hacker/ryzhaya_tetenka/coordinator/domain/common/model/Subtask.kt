package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Subtask(
    val id: Int = 0,
    val taskId: Int = 0,
    val title: String = "",
    var checked: MutableState<Boolean> = mutableStateOf(false)
)
