package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType
import java.util.Date

class TaskState {
    var timeTypeId by mutableStateOf(0)
    var timeType by mutableStateOf(TimeType())

    var date by mutableStateOf(Date())
    var weekDate by mutableStateOf(Pair(Date(), Date()))

    var taskId by mutableStateOf(0)
    var task by mutableStateOf(Task())

    var showTaskMenu by mutableStateOf(false)
    var showQuestionDialog by mutableStateOf(false)

    var success by mutableStateOf(false)
}