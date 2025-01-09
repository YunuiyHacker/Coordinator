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
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.CategoriesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.TasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.startAndEndThisWeek
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val tasksUseCase: TasksUseCase,
    private val categoriesUseCase: CategoriesUseCase
) : ViewModel() {
    val state by mutableStateOf(TaskState())

    fun onEvent(event: TaskEvent) {
        when (event) {
            TaskEvent.LoadDataEvent -> loadData()

            TaskEvent.ShowTaskMenuEvent -> state.showTaskMenu = true
            TaskEvent.HideTaskMenuEvent -> state.showTaskMenu = false

            TaskEvent.ShowQuestionDialogEvent -> state.showQuestionDialog = true
            TaskEvent.HideQuestionDialogEvent -> state.showQuestionDialog = false

            TaskEvent.DeleteTaskEvent -> deleteTask()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        GlobalScope.launch {
            runBlocking {
                state.task = tasksUseCase.getTaskByIdOperator(state.taskId).toDomain()

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
}