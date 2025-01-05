package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_update_task

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
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.TasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.setCalendarTime
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.setDateTime
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.startAndEndThisWeek
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class CreateUpdateTaskViewModel @Inject constructor(private val tasksUseCase: TasksUseCase) :
    ViewModel() {
    val state by mutableStateOf(CreateUpdateTaskState())

    fun onEvent(event: CreateUpdateTaskEvent) {
        when (event) {
            is CreateUpdateTaskEvent.LoadDataEvent -> loadData()

            is CreateUpdateTaskEvent.HeadingChangeEvent -> {
                state.heading = event.heading
            }

            is CreateUpdateTaskEvent.ContentChangeEvent -> {
                state.content = event.content
            }

            is CreateUpdateTaskEvent.ShowDatePickerDialogEvent -> state.showDatePickerDialog = true
            is CreateUpdateTaskEvent.SelectDatePickerDialogEvent -> changeDate(event)
            is CreateUpdateTaskEvent.HideDatePickerDialogEvent -> state.showDatePickerDialog = false

            is CreateUpdateTaskEvent.ShowTimePickerDialogEvent -> state.showTimePickerDialog = true
            is CreateUpdateTaskEvent.SelectTimePickerDialogEvent -> changeTime(event)
            is CreateUpdateTaskEvent.HideTimePickerDialogEvent -> state.showTimePickerDialog = false

            is CreateUpdateTaskEvent.ShowTimeTypePickerMenuEvent -> state.showTimeTypePickerMenu =
                true

            is CreateUpdateTaskEvent.SelectTimeTypePickerMenuEvent -> changeTimeType(event)
            is CreateUpdateTaskEvent.HideTimeTypePickerMenuEvent -> state.showTimeTypePickerMenu =
                false

            is CreateUpdateTaskEvent.OnClickButtonEvent -> createOrUpdateTask()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        if (state.taskId == 0) {
            state.timeType = Constants.timeTypes.find { state.timeTypeId == it.id }!!
            state.date = setDateTime(Date(state.dateInMilliseconds))
        }

        GlobalScope.launch {
            runBlocking {
                if (state.taskId != 0) {
                    state.task = tasksUseCase.getTaskByIdOperator.invoke(state.taskId).toDomain()

                    state.heading = state.task.title
                    state.content = state.task.content
                    state.timeTypeId = state.task.timeTypeId
                    state.timeType =
                        Constants.timeTypes.find { timeType -> timeType.id == state.timeTypeId }!!
                    state.dateInMilliseconds = state.task.date.time
                    state.date = state.task.date
                    state.selectedHour = state.task.hour
                    state.selectedMinute = state.task.minute

                    val c: Calendar = GregorianCalendar()
                    c.timeInMillis = state.date.time
                    state.weekDate = startAndEndThisWeek(c)
                }
            }
        }
    }

    private fun changeDate(event: CreateUpdateTaskEvent.SelectDatePickerDialogEvent) {
        state.selectedDateInMilliseconds = event.dateInMilliseconds
        state.date = setDateTime(Date(state.selectedDateInMilliseconds))

        var c: Calendar = GregorianCalendar()
        c.timeInMillis = state.selectedDateInMilliseconds
        c = setCalendarTime(c)
        state.weekDate = startAndEndThisWeek(c)

        state.showDatePickerDialog = false
    }

    private fun changeTime(event: CreateUpdateTaskEvent.SelectTimePickerDialogEvent) {
        state.selectedHour = event.hour
        state.selectedMinute = event.minute

        state.showTimePickerDialog = false
    }

    private fun changeTimeType(event: CreateUpdateTaskEvent.SelectTimeTypePickerMenuEvent) {
        state.timeType = event.timeType

        state.showTimeTypePickerMenu = false
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun createOrUpdateTask() {
        val task = Task(
            id = state.taskId,
            timeTypeId = state.timeType.id,
            date = if (state.selectedDateInMilliseconds > 0) Date(state.selectedDateInMilliseconds) else state.date,
            hour = state.selectedHour,
            minute = state.selectedMinute,
            title = state.heading,
            content = state.content
        ).toData()

        GlobalScope.launch(Dispatchers.IO) {
            if (state.taskId == 0) {
                tasksUseCase.insertTaskOperator.invoke(task)
            } else {
                tasksUseCase.updateTaskOperator.invoke(task)
            }
        }

        state.success = true
    }
}