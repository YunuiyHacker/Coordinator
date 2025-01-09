package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_update_task

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toData
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toDomain
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.CategoriesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.TasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.setCalendarTime
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.setDateTime
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.startAndEndThisWeek
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class CreateUpdateTaskViewModel @Inject constructor(
    private val tasksUseCase: TasksUseCase,
    private val categoriesUseCase: CategoriesUseCase,
    private val application: Application
) : ViewModel() {
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

            is CreateUpdateTaskEvent.ShowEndTimePickerDialogEvent -> state.showEndTimePickerDialog =
                true

            is CreateUpdateTaskEvent.SelectEndTimePickerDialogEvent -> changeEndTime(event)
            is CreateUpdateTaskEvent.HideEndTimePickerDialogEvent -> state.showEndTimePickerDialog =
                false

            is CreateUpdateTaskEvent.EndTimeCheckToggleEvent -> state.endTimeChecked =
                !state.endTimeChecked

            is CreateUpdateTaskEvent.ShowTaskMenuEvent -> state.showTaskMenu = true
            is CreateUpdateTaskEvent.HideTaskMenuEvent -> state.showTaskMenu = false

            is CreateUpdateTaskEvent.ShowMessageDialogEvent -> state.showMessageDialog = true
            is CreateUpdateTaskEvent.HideMessageDialogEvent -> state.showMessageDialog = false

            is CreateUpdateTaskEvent.ShowTimeTypePickerMenuEvent -> state.showTimeTypePickerMenu =
                true

            is CreateUpdateTaskEvent.SelectTimeTypePickerMenuEvent -> changeTimeType(event)
            is CreateUpdateTaskEvent.HideTimeTypePickerMenuEvent -> state.showTimeTypePickerMenu =
                false

            is CreateUpdateTaskEvent.ShowCategorySelectorMenuEvent -> state.showCategorySelectorMenu =
                true

            is CreateUpdateTaskEvent.SelectCategoryMenuEvent -> changeCategory(event)

            is CreateUpdateTaskEvent.HideCategorySelectorMenuEvent -> state.showCategorySelectorMenu =
                false

            is CreateUpdateTaskEvent.OnClickButtonEvent -> createOrUpdateTask()
        }
    }

    init {
        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {

                state.contentState.isLoading.value = true
                state.categories.add(Category(0, application.getString(R.string.without_category)))
                state.categories.addAll(
                    categoriesUseCase.getCategoriesOperator.invoke().map { category ->
                        category.toDomain()
                    }.toMutableList()
                )

                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        state.contentState.isLoading.value = true

        if (state.taskId == 0) {
            state.timeType = Constants.timeTypes.find { state.timeTypeId == it.id }!!
            state.date = setDateTime(Date(state.dateInMilliseconds))
            state.selectedCategory = Category(0, application.getString(R.string.without_category))
        }

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                if (state.taskId != 0) {
                    state.task = tasksUseCase.getTaskByIdOperator.invoke(state.taskId).toDomain()

                    state.heading = state.task.title
                    state.content = state.task.content
                    state.timeTypeId = state.task.timeTypeId
                    state.timeType =
                        Constants.timeTypes.find { timeType -> timeType.id == state.timeTypeId }!!
                    state.selectedCategory =
                        state.categories.find { category -> category.id == state.task.categoryId }!!
                    state.dateInMilliseconds = state.task.date.time
                    state.date = state.task.date
                    state.selectedHour = state.task.hour
                    state.selectedMinute = state.task.minute
                    state.endTimeChecked = state.task.withEndTime
                    state.selectedEndHour = state.task.endHour
                    state.selectedEndMinute = state.task.endMinute

                    val c: Calendar = GregorianCalendar()
                    c.timeInMillis = state.date.time
                    state.weekDate = startAndEndThisWeek(c)
                }

                state.contentState.isLoading.value = false
            }
        }
    }

    private fun changeCategory(event: CreateUpdateTaskEvent.SelectCategoryMenuEvent) {
        state.selectedCategory =
            event.category

        state.showCategorySelectorMenu = false
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

    private fun changeEndTime(event: CreateUpdateTaskEvent.SelectEndTimePickerDialogEvent) {
        state.selectedEndHour = event.hour
        state.selectedEndMinute = event.minute

        state.showEndTimePickerDialog = false
    }

    private fun changeTimeType(event: CreateUpdateTaskEvent.SelectTimeTypePickerMenuEvent) {
        state.timeType = event.timeType

        state.showTimeTypePickerMenu = false
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun createOrUpdateTask() {
        state.contentState.isLoading.value = true

        val task = Task(
            id = state.taskId,
            categoryId = state.selectedCategory.id,
            timeTypeId = state.timeType.id,
            date = if (state.selectedDateInMilliseconds > 0) Date(state.selectedDateInMilliseconds) else state.date,
            hour = state.selectedHour,
            minute = state.selectedMinute,
            withEndTime = state.endTimeChecked,
            endHour = state.selectedEndHour,
            endMinute = state.selectedEndMinute,
            title = state.heading,
            content = state.content
        ).toData()

        if (state.endTimeChecked) {
            if ((state.selectedEndHour > state.selectedHour || (state.selectedEndHour == state.selectedHour && state.selectedEndMinute > state.selectedMinute))) {

                GlobalScope.launch(Dispatchers.IO) {
                    if (state.taskId == 0) {
                        tasksUseCase.insertTaskOperator.invoke(task)
                    } else {
                        tasksUseCase.updateTaskOperator.invoke(task)
                    }
                    state.contentState.isLoading.value = false
                    state.success = true
                }
            } else {
                state.contentState.exception.value = Exception(
                    application.getString(R.string.end_time_can_not_be_before_start_time)
                )
                state.showMessageDialog = true
            }
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                if (state.taskId == 0) {
                    tasksUseCase.insertTaskOperator.invoke(task)
                } else {
                    tasksUseCase.updateTaskOperator.invoke(task)
                }

                state.contentState.isLoading.value = false
                state.success = true
            }
        }
    }
}