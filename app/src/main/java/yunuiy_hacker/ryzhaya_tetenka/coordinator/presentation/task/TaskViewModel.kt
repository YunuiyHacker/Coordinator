package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toData
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toDomain
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples.PeoplesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples_in_tasks.PeoplesInTasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.PlacesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.PlacesInTasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.SubtasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.TasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.startAndEndThisWeek
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.stream.Collectors.toList
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val tasksUseCase: TasksUseCase,
    private val placesUseCase: PlacesUseCase,
    private val placesInTasksUseCase: PlacesInTasksUseCase,
    private val subtasksUseCase: SubtasksUseCase,
    private val peoplesUseCase: PeoplesUseCase,
    private val peoplesInTasksUseCase: PeoplesInTasksUseCase
) : ViewModel() {
    val state by mutableStateOf(TaskState())

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.LoadDataEvent -> loadData()

            is TaskEvent.ShowTaskMenuEvent -> state.showTaskMenu = true
            is TaskEvent.HideTaskMenuEvent -> state.showTaskMenu = false

            is TaskEvent.ShowQuestionDialogEvent -> {
                state.questionTitle = event.title
                state.questionText = event.text
                state.showQuestionDialog = true
            }

            is TaskEvent.HideQuestionDialogEvent -> state.showQuestionDialog = false

            is TaskEvent.OnOpenPlaceInMapModeEvent -> state.openPlaceInMapMode = true
            is TaskEvent.OpenPlaceInMapEvent -> {
                state.showQuestionDialog = false
            }

            is TaskEvent.OffOpenPlaceInMapModeEvent -> state.openPlaceInMapMode = false

            is TaskEvent.SubtaskItemCheckboxToggleEvent -> subtaskItemToggle(event.subtask)
            is TaskEvent.DeleteTaskEvent -> deleteTask()

            is TaskEvent.ShowCreateUpdatePeopleBottomSheetEvent -> {
                state.showCreateUpdatePeopleBottomSheet = true
                state.selectedPeople = event.people
            }

            is TaskEvent.HideCreateUpdatePeopleBottomSheetEvent -> state.showCreateUpdatePeopleBottomSheet =
                false
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                state.task = tasksUseCase.getTaskByIdOperator(state.taskId).toDomain()
                state.subtasks = subtasksUseCase.getSubtasksByTaskIdOperator(state.task.id)
                    .map { subtask -> subtask.toDomain() }.toMutableList()
                val dataPlace = placesUseCase.getPlaceByIdOperator(
                    placesInTasksUseCase.getPlacesInTaskByTaskId(state.taskId)?.placeId ?: 0
                )
                if (dataPlace != null) state.place = dataPlace.toDomain()
                val peoplesInTasks =
                    peoplesInTasksUseCase.getPeoplesInTasksByTaskIdOperator(state.taskId)
                val localPeoples: MutableList<People> = mutableListOf()
                for (i in 0..<peoplesInTasks.size) {
                    val localPeople =
                        peoplesUseCase.getPeopleByIdOperator(peoplesInTasks[i].peopleId!!)
                    if (localPeople != null)
                        localPeoples.add(
                            localPeople.toDomain()
                        )
                }
                state.peoples = localPeoples.toList()

                val calendar: Calendar = GregorianCalendar()
                calendar.timeInMillis = state.task.date.time
                state.weekDate = startAndEndThisWeek(calendar)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun deleteTask() {
        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                tasksUseCase.deleteTaskOperator.invoke(state.task.toData())

                state.showQuestionDialog = false
                state.success = true
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun subtaskItemToggle(subtask: Subtask) {
        GlobalScope.launch(Dispatchers.IO) {
            subtasksUseCase.updateSubtaskOperator.invoke(
                subtask.copy(
                    checked = mutableStateOf(
                        subtask.checked.value
                    )
                ).toData()
            )
        }
        subtask.checked.value = !subtask.checked.value
    }
}