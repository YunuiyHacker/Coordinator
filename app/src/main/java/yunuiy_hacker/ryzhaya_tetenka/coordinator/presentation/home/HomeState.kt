package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeOfDay
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType
import java.util.Date

class HomeState {
    var timeOfDay by mutableStateOf(TimeOfDay.MORNING)
    var timeType by mutableStateOf(TimeType())
    var userName by mutableStateOf("")

    var todayDate by mutableStateOf(Date())
    var todayWeekStart by mutableStateOf(Date())
    var todayWeekEnd by mutableStateOf(Date())
    var todayMonth by mutableStateOf(Date())
    var todayYear by mutableStateOf(Date())
    var selectedDate by mutableStateOf(Date())
    var selectedWeekStart by mutableStateOf(Date())
    var selectedWeekEnd by mutableStateOf(Date())
    var selectedMonth by mutableStateOf(Date())
    var selectedYear by mutableStateOf(Date())

    var daysList = mutableStateListOf<Date>()
    var weeksList = mutableStateListOf<Pair<Date, Date>>()
    var monthsList = mutableStateListOf<Date>()
    var yearsList = mutableStateListOf<Date>()

    var timeTypeRowVisible by mutableStateOf(false)
}