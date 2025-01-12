package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator.Companion.invoke
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toData
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toDomain
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.CategoriesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.SubtasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.TasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.HomeEvent
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.startAndEndThisWeek
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val tasksUseCase: TasksUseCase,
    private val categoriesUseCase: CategoriesUseCase,
    private val subtasksUseCase: SubtasksUseCase
) : ViewModel() {
    val state by mutableStateOf(TaskState())

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.LoadDataEvent -> loadData()

            is TaskEvent.ShowTaskMenuEvent -> state.showTaskMenu = true
            is TaskEvent.HideTaskMenuEvent -> state.showTaskMenu = false

            is TaskEvent.ShowQuestionDialogEvent -> state.showQuestionDialog = true
            is TaskEvent.HideQuestionDialogEvent -> state.showQuestionDialog = false

            is TaskEvent.SubtaskItemCheckboxToggleEvent -> subtaskItemToggle(event.subtask)
            is TaskEvent.DeleteTaskEvent -> deleteTask()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        GlobalScope.launch {
            runBlocking {
                state.task = tasksUseCase.getTaskByIdOperator(state.taskId).toDomain()
                state.subtasks = subtasksUseCase.getSubtasksByTaskId(state.task.id)
                    .map { subtask -> subtask.toDomain() }.toMutableList()

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