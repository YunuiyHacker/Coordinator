package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.state.ContentState
import java.util.Date

class TaskState {
    var timeTypeId by mutableStateOf(0)
    var timeType by mutableStateOf(TimeType())

    var date by mutableStateOf(Date())
    var weekDate by mutableStateOf(Pair(Date(), Date()))

    var taskId by mutableStateOf(0)
    var task by mutableStateOf(Task())
    var subtasks: MutableList<Subtask> = mutableListOf()
    var place by mutableStateOf(Place())
    var peoples: List<People> = listOf()

    var showTaskMenu by mutableStateOf(false)
    var questionTitle by mutableStateOf("")
    var questionText by mutableStateOf("")
    var showQuestionDialog by mutableStateOf(false)

    var openPlaceInMapMode by mutableStateOf(false)

    var selectedPeople by mutableStateOf(People())
    var showCreateUpdatePeopleBottomSheet by mutableStateOf(false)

    var success by mutableStateOf(false)

    var contentState by mutableStateOf(ContentState())
}