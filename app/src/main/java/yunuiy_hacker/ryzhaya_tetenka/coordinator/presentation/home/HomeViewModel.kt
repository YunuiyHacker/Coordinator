package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toData
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toDomain
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.TasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.use_case.DefineTimeOfDayUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.setCalendarTime
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.setDateTime
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.startAndEndThisWeek
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.toTimeType
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.toTimeTypeEvent
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.zoneOffset
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPrefsHelper: SharedPrefsHelper,
    private val defineTimeOfDayUseCase: DefineTimeOfDayUseCase,
    private val tasksUseCase: TasksUseCase
) : ViewModel() {
    val state by mutableStateOf(HomeState())

    init {
        state.timeOfDay = defineTimeOfDayUseCase.execute()
        state.userName = sharedPrefsHelper.name ?: ""
        state.timeType = sharedPrefsHelper.timeTypeEnum.toTimeType()

        state.selectedDate = setDateTime(Date())
        var c: Calendar = GregorianCalendar()
        c.timeInMillis = state.selectedDate.time
        c = setCalendarTime(c)
        val startAndEndThisWeek = startAndEndThisWeek(c)
        state.selectedWeekStart = startAndEndThisWeek.first
        state.selectedWeekEnd = startAndEndThisWeek.second
        state.selectedMonth = setDateTime(Date())
        state.selectedYear = setDateTime(Date())
        initData()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadDataEvent -> loadData()

            is HomeEvent.ShowDatePickerDialogEvent -> {
                state.showDatePickerDialog = true
                state.showLazySwipeColumn = false
            }

            is HomeEvent.SelectDatePickerDialogEvent -> {
                changeDate(event)
                state.showLazySwipeColumn = true
            }

            is HomeEvent.HideDatePickerDialogEvent -> {
                state.showDatePickerDialog = false
                state.showLazySwipeColumn = true
            }

            is HomeEvent.SelectedDateChangeEvent -> loadData()
            is HomeEvent.TimeTypeChangeEvent -> {
                sharedPrefsHelper.timeTypeEnum = event.timeType.toTimeTypeEvent()
                state.timeType = event.timeType

                loadData()
            }

            is HomeEvent.SearchQueryChangeEvent -> {
                state.query = event.query

                if (state.query.isEmpty()) loadData()
            }

            is HomeEvent.OnDeletionModeEvent -> state.isDeletionMode = true
            is HomeEvent.OffDeletionModeEvent -> {
                state.isDeletionMode = false
                state.deletionTasks.clear()
            }

            is HomeEvent.ShowQuestionDialogEvent -> state.showQuestionDialog = true
            is HomeEvent.HideQuestionDialogEvent -> state.showQuestionDialog = false
            is HomeEvent.SelectAllEvent -> selectAll()
            is HomeEvent.UnselectAllEvent -> unselectAll()
            is HomeEvent.AddSelectedTaskEvent -> {
                state.deletionTasks.add(event.task)
                if (state.deletionTasks.size == state.tasks.size) state.selectedAll = true
                else state.selectedAll = false
            }

            is HomeEvent.RemoveSelectedTaskEvent -> {
                state.deletionTasks.remove(event.task)
                if (state.deletionTasks.size == state.tasks.size) state.selectedAll = true
                else state.selectedAll = false
            }

            is HomeEvent.DeleteAllSelectedTasksEvent -> deleteAllSelectedTasks()

            is HomeEvent.OnClickSearchEvent -> searchTasks(state.query)

            is HomeEvent.TaskItemCheckboxToggleEvent -> taskItemToggle(event)

            is HomeEvent.OnClickAddNewTaskEvent -> {

            }
        }
    }

    private fun initData() {
        fillDaysToDateList()
        fillWeeksToDateList()
        fillMonthsToDateList()
        fillYearsToDateList()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                state.tasks = tasksUseCase.getTasksByTimeTypeIdAndDateOperator.invoke(
                    timeTypeId = state.timeType.id,
                    dateInMilliseconds = when (state.timeType.toTimeTypeEvent()) {
                        TimeTypeEnum.DAY -> state.selectedDate.time
                        TimeTypeEnum.WEEK -> 0
                        TimeTypeEnum.MONTH -> state.selectedMonth.time
                        TimeTypeEnum.YEAR -> state.selectedYear.time
                        TimeTypeEnum.LIFE -> 0
                    },
                    dates = Pair(state.selectedWeekStart, state.selectedWeekEnd)
                ).map { task -> task.toDomain() }.toMutableList()

                state.contentState.isLoading.value = false
            }
        }
    }

    private fun selectAll() {
        for (i in 0..<state.tasks.size) {
            if (!state.deletionTasks.contains(state.tasks[i])) state.deletionTasks.add(state.tasks[i])
        }

        state.selectedAll = true
    }

    private fun unselectAll() {
        state.deletionTasks.clear()

        state.selectedAll = false
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun deleteAllSelectedTasks() {
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                for (i in 0..<state.deletionTasks.size) {
                    tasksUseCase.deleteTaskOperator.invoke(state.deletionTasks[i].toData())
                    state.tasks.remove(state.deletionTasks[i])
                }

                state.isDeletionMode = false
                state.showQuestionDialog = false
                state.contentState.isLoading.value = false
            }
        }
    }

    private fun changeDate(event: HomeEvent.SelectDatePickerDialogEvent) {
        state.selectedDateInMilliseconds =
            event.dateInMilliseconds + (zoneOffset.totalSeconds * 1000)

        state.selectedDate = Date(state.selectedDateInMilliseconds)
        state.selectedMonth = Date(state.selectedDateInMilliseconds)
        state.selectedYear = Date(state.selectedDateInMilliseconds)

        val c = GregorianCalendar()
        c.timeInMillis = state.selectedDateInMilliseconds
        val startAndEndThisWeek = startAndEndThisWeek(c)
        state.selectedWeekStart = startAndEndThisWeek.first
        state.selectedWeekEnd = startAndEndThisWeek.second

        initData()
        loadData()
        state.showDatePickerDialog = false
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun searchTasks(query: String) {
        if (query.isNotEmpty()) {
            state.contentState.isLoading.value = true

            GlobalScope.launch(Dispatchers.IO) {
                runBlocking {
                    state.tasks = tasksUseCase.getTasksByLikeQueryOperator.invoke(query)
                        .map { task -> task.toDomain() }.toMutableList()

                    state.contentState.isLoading.value = false
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun taskItemToggle(event: HomeEvent.TaskItemCheckboxToggleEvent) {
        GlobalScope.launch(Dispatchers.IO) {
            tasksUseCase.updateTaskOperator.invoke(
                event.task.copy(
                    checked = mutableStateOf(
                        event.task.checked.value
                    )
                ).toData()
            )
        }
        event.task.checked.value = !event.task.checked.value
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun fillDaysToDateList() {
        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                state.daysList.clear()

                var c: Calendar = GregorianCalendar()
                c.timeInMillis = state.selectedDate.time
                c = setCalendarTime(c)
                c.add(Calendar.DAY_OF_MONTH, -20)

                for (i in 0..<40) {
                    c.add(Calendar.DAY_OF_MONTH, +1)
                    state.daysList.add(Date(c.timeInMillis))
                }
            }
        }
    }

    private fun fillWeeksToDateList() {
        state.weeksList.clear()

        var c: Calendar = GregorianCalendar()
        c.timeInMillis = state.selectedDate.time
        c = setCalendarTime(c)
        c.add(Calendar.WEEK_OF_YEAR, -19)

        for (i in 0..<40) {
            state.weeksList.add(startAndEndThisWeek(c))
            c.add(Calendar.WEEK_OF_YEAR, +1)
        }
    }

    private fun fillMonthsToDateList() {
        state.monthsList.clear()

        var c: Calendar = GregorianCalendar()
        c.timeInMillis = state.selectedDate.time
        c = setCalendarTime(c)
        c.add(Calendar.MONTH, -20)

        for (i in 0..<40) {
            c.add(Calendar.MONTH, +1)
            state.monthsList.add(Date(c.timeInMillis))
        }
    }

    private fun fillYearsToDateList() {
        state.yearsList.clear()

        var c: Calendar = GregorianCalendar()
        c.timeInMillis = state.selectedDate.time
        c = setCalendarTime(c)
        c.add(Calendar.YEAR, -20)

        for (i in 0..<40) {
            c.add(Calendar.YEAR, +1)
            state.yearsList.add(Date(c.timeInMillis))
        }
    }
}