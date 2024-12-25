package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.use_case.DefineTimeOfDayUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.startAndEndThisWeek
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.toTimeType
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.toTimeTypeEvent
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPrefsHelper: SharedPrefsHelper,
    private val defineTimeOfDayUseCase: DefineTimeOfDayUseCase
) : ViewModel() {
    val state by mutableStateOf(HomeState())

    init {
        state.timeOfDay = defineTimeOfDayUseCase.execute()
        state.userName = sharedPrefsHelper.name ?: ""
        state.timeType = sharedPrefsHelper.timeTypeEnum.toTimeType()

        state.todayDate = Date()
        state.selectedDate = Date()
        val c: GregorianCalendar = GregorianCalendar()
        c.timeInMillis = state.selectedDate.time
        val startAndEndThisWeek = startAndEndThisWeek(c)
        state.selectedWeekStart = startAndEndThisWeek[0]
        state.selectedWeekEnd = startAndEndThisWeek[1]
        state.todayWeekStart = startAndEndThisWeek[0]
        state.todayWeekEnd = startAndEndThisWeek[1]
        state.todayMonth = Date()
        state.selectedMonth = Date()
        state.todayYear = Date()
        state.selectedYear = Date()

        fillDaysToDateList()
        fillWeeksToDateList()
        fillMonthsToDateList()
        fillYearsToDateList()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.TimeTypeChangeEvent -> {
                sharedPrefsHelper.timeTypeEnum = event.timeType.toTimeTypeEvent()
                state.timeType = event.timeType
            }
        }
    }

    fun fillDaysToDateList() {
        val c: GregorianCalendar = GregorianCalendar()
        c.timeInMillis = state.todayDate.time
        c.add(Calendar.DAY_OF_MONTH, -20)

        for (i in 0..<40) {
            c.add(Calendar.DAY_OF_MONTH, +1)
            state.daysList.add(Date(c.timeInMillis))
        }
    }

    fun fillWeeksToDateList() {
        val c: GregorianCalendar = GregorianCalendar()
        c.timeInMillis = state.todayDate.time
        c.add(Calendar.WEEK_OF_YEAR, -19)

        for (i in 0..<40) {
            val startAndEndThisWeek = startAndEndThisWeek(c)
            state.weeksList.add(Pair(startAndEndThisWeek[0], startAndEndThisWeek[1]))
            c.add(Calendar.WEEK_OF_YEAR, +1)
        }
    }

    fun fillMonthsToDateList() {
        val c: GregorianCalendar = GregorianCalendar()
        c.timeInMillis = state.todayDate.time
        c.add(Calendar.MONTH, -20)

        for (i in 0..<40) {
            c.add(Calendar.MONTH, +1)
            state.monthsList.add(Date(c.timeInMillis))
        }
    }

    fun fillYearsToDateList() {
        val c: GregorianCalendar = GregorianCalendar()
        c.timeInMillis = state.todayDate.time
        c.add(Calendar.YEAR, -20)

        for (i in 0..<40) {
            c.add(Calendar.YEAR, +1)
            state.yearsList.add(Date(c.timeInMillis))
        }
    }
}