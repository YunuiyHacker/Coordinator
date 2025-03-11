package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_update_task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.state.ContentState
import java.util.Date

class CreateUpdateTaskState {
    var timeTypeId by mutableStateOf(0)
    var timeType by mutableStateOf(TimeType())
    var dateInMilliseconds by mutableStateOf(0L)
    var date by mutableStateOf(Date())
    var weekDate by mutableStateOf(Pair<Date, Date>(Date(), Date()))

    var taskId by mutableStateOf(0)
    var task by mutableStateOf(Task())

    var heading by mutableStateOf("")
    var content by mutableStateOf("")
    var checked by mutableStateOf(false)

    var showDatePickerDialog by mutableStateOf(false)
    var selectedDateInMilliseconds by mutableStateOf(0L)

    var showTimePickerDialog by mutableStateOf(false)
    var selectedHour by mutableStateOf(0)
    var selectedMinute by mutableStateOf(0)

    var showTaskMenu by mutableStateOf(false)
    var endTimeChecked by mutableStateOf(false)
    var showEndTimePickerDialog by mutableStateOf(false)
    var selectedEndHour by mutableStateOf(0)
    var selectedEndMinute by mutableStateOf(0)

    var showTimeTypePickerMenu by mutableStateOf(false)

    var showMessageDialog by mutableStateOf(false)

    var categoryId by mutableStateOf(0)
    var selectedCategory by mutableStateOf(Category())
    var categories: MutableList<Category> = mutableListOf()
    var showCategorySelectorMenu by mutableStateOf(false)

    var subtasks: MutableList<Subtask> = mutableListOf()

    var selectedPlace by mutableStateOf(Place(id = 0))
    var showPlacesSelectorSheet by mutableStateOf(false)
    var places: MutableList<Place> = mutableListOf()
    var showCreateUpdatePlaceDialog by mutableStateOf(false)

    var showPeoplesSelectorSheet by mutableStateOf(false)
    var peoples: MutableList<People> = mutableListOf()
    var selectedPeoples: MutableList<People> = mutableListOf()

    var remindLaterIsChecked by mutableStateOf(false)
    var selectNotifyDateOrTime by mutableStateOf(false)
    var notifyDate by mutableStateOf(Date())
    var notifyHour by mutableStateOf(0)
    var notifyMinute by mutableStateOf(0)

    var showNotificationPermissionInfoDialog by mutableStateOf(false)

    val contentState by mutableStateOf(ContentState())

    var success by mutableStateOf(false)
}