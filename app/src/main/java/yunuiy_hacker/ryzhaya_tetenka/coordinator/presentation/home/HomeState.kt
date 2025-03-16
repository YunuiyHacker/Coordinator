package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeOfDay
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.state.ContentState
import java.util.Date

class HomeState {
    var timeOfDay by mutableStateOf(TimeOfDay.MORNING)
    var timeType by mutableStateOf(TimeType())
    var userName by mutableStateOf("")

    var selectedDate by mutableStateOf(Date())
    var selectedWeekStart by mutableStateOf(Date())
    var selectedWeekEnd by mutableStateOf(Date())
    var selectedMonth by mutableStateOf(Date())
    var selectedYear by mutableStateOf(Date())

    var daysList = mutableListOf<Date>()
    var weeksList = mutableListOf<Pair<Date, Date>>()
    var monthsList = mutableListOf<Date>()
    var yearsList = mutableListOf<Date>()

    var query by mutableStateOf("")

    var timeTypeRowVisible by mutableStateOf(false)

    var tasks: MutableList<Task> = mutableListOf()
    var categories: MutableList<Category> = mutableListOf()

    var defaultAllCategoriesValue by mutableStateOf(Category())
    var selectedCategory by mutableStateOf(Category())
    var editionDeletionCategory by mutableStateOf(Category())
    var isEditMode by mutableStateOf(false)

    var showDatePickerDialog by mutableStateOf(false)
    var selectedDateInMilliseconds by mutableStateOf(0L)
    var showLazySwipeColumn by mutableStateOf(true)
    var showAddEditCategoryDialog by mutableStateOf(false)

    var isDeletionMode by mutableStateOf(false)
    var deletionTasks: MutableList<Task> = mutableStateListOf()
    var selectedAll by mutableStateOf(false)

    var questionTitle by mutableStateOf("")
    var questionText by mutableStateOf("")
    var showQuestionDialog by mutableStateOf(false)

    var showCategoryMenu by mutableStateOf(false)

    var showTaskPriority by mutableStateOf(false)
    var showTasksStatistics by mutableStateOf(false)

    var urgentAndImportantTasks: MutableList<Task> = mutableListOf()
    var notUrgentAndImportantTasks: MutableList<Task> = mutableListOf()
    var urgentAndUnimportantTasks: MutableList<Task> = mutableListOf()
    var notUrgentAndUnimportantTasks: MutableList<Task> = mutableListOf()
    var notPriorityTasks: MutableList<Task> = mutableListOf()

    var completedTasks by mutableStateOf(0)
    var notCompletedTasks by mutableStateOf(0)
    var allCompletedTasks by mutableStateOf(0)
    var allNotCompletedTasks by mutableStateOf(0)

    var contentState by mutableStateOf(ContentState())
}