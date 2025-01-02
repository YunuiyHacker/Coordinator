package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.startAndEndThisWeek
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor() : ViewModel() {
    val state by mutableStateOf(CreateTaskState())

    fun onEvent(event: CreateTaskEvent) {
        when (event) {
            is CreateTaskEvent.InitEvent -> init()

            is CreateTaskEvent.HeadingChangeEvent -> {
                state.heading = event.heading
            }

            is CreateTaskEvent.ContentChangeEvent -> {
                state.content = event.content
            }

            is CreateTaskEvent.ShowDatePickerDialogEvent -> state.showDatePickerDialog = true
            is CreateTaskEvent.SelectDatePickerDialogEvent -> changeDate(event)
            is CreateTaskEvent.HideDatePickerDialogEvent -> state.showDatePickerDialog = false

            is CreateTaskEvent.ShowTimePickerDialogEvent -> state.showTimePickerDialog = true
            is CreateTaskEvent.SelectTimePickerDialogEvent -> changeTime(event)
            is CreateTaskEvent.HideTimePickerDialogEvent -> state.showTimePickerDialog = false

            is CreateTaskEvent.ShowTimeTypePickerMenuEvent -> state.showTimeTypePickerMenu = true
            is CreateTaskEvent.SelectTimeTypePickerMenuEvent -> changeTimeType(event)
            is CreateTaskEvent.HideTimeTypePickerMenuEvent -> state.showTimeTypePickerMenu = false

            is CreateTaskEvent.OnClickButtonEvent -> createTask()
        }
    }

    private fun init() {
        state.timeType = Constants.timeTypes.find { state.timeTypeId == it.id }!!
        state.date = Date(state.dateInMilliseconds)
    }

    private fun changeDate(event: CreateTaskEvent.SelectDatePickerDialogEvent) {
        state.selectedDateInMilliseconds = event.dateInMilliseconds
        state.date = Date(state.selectedDateInMilliseconds)

        val c = GregorianCalendar()
        c.timeInMillis = state.selectedDateInMilliseconds
        val startAndEndThisWeek = startAndEndThisWeek(c)
        state.weekDate = Pair(startAndEndThisWeek[0], startAndEndThisWeek[1])

        state.showDatePickerDialog = false
    }

    private fun changeTime(event: CreateTaskEvent.SelectTimePickerDialogEvent) {
        state.selectedHour = event.hour
        state.selectedMinute = event.minute

        state.showTimePickerDialog = false
    }

    private fun changeTimeType(event: CreateTaskEvent.SelectTimeTypePickerMenuEvent) {
        state.timeType = event.timeType

        state.showTimeTypePickerMenu = false
    }

    private fun createTask() {
        state.success = true
    }
}