package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType
import java.util.Date

class CreateTaskState {
    var timeTypeId by mutableStateOf(0)
    var timeType by mutableStateOf(TimeType())
    var dateInMilliseconds by mutableStateOf(0L)
    var date by mutableStateOf(Date())
    var weekDate by mutableStateOf(Pair<Date, Date>(Date(), Date()))

    var heading by mutableStateOf("")
    var content by mutableStateOf("")

    var showDatePickerDialog by mutableStateOf(false)
    var selectedDateInMilliseconds by mutableStateOf(0L)

    var showTimePickerDialog by mutableStateOf(false)
    var selectedHour by mutableStateOf(0)
    var selectedMinute by mutableStateOf(0)

    var showTimeTypePickerMenu by mutableStateOf(false)

    var success by mutableStateOf(false)
}