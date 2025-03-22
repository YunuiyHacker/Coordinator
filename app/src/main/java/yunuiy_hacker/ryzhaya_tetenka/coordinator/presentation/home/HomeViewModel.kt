package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Priority
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toData
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toDomain
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Notification
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.CategoriesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.notifications.NotificationsUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.SubtasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.TasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.use_case.DefineTimeOfDayUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.getLocaleStringResource
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.setCalendarTime
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.setDateTime
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.startAndEndThisWeek
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.toTimeType
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.toTimeTypeEvent
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.zoneOffset
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val sharedPrefsHelper: SharedPrefsHelper,
    private val defineTimeOfDayUseCase: DefineTimeOfDayUseCase,
    private val tasksUseCase: TasksUseCase,
    private val subtasksUseCase: SubtasksUseCase,
    private val categoriesUseCase: CategoriesUseCase,
    private val notificationsUseCase: NotificationsUseCase,
    private val application: Application
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

        state.defaultAllCategoriesValue = Category(
            id = 0, title = getLocaleStringResource(
                application.resources.configuration.locale,
                R.string.all_categories,
                application as Context
            )
        )

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

            is HomeEvent.SelectedDateChangeEvent -> {
                loadData()
            }

            is HomeEvent.TimeTypeChangeEvent -> {
                state.timeType = event.timeType
                loadData()
                sharedPrefsHelper.timeTypeEnum = event.timeType.toTimeTypeEvent()
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

            is HomeEvent.ShowQuestionDialogEvent -> {
                state.questionTitle = event.title
                state.questionText = event.text
                state.showQuestionDialog = true
            }

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

            is HomeEvent.SelectCategoryEvent -> selectCategory(event)

            is HomeEvent.ShowAddCategoryDialogEvent -> state.showAddEditCategoryDialog = true
            is HomeEvent.HideAddCategoryDialogEvent -> state.showAddEditCategoryDialog = false
            is HomeEvent.CreateCategoryEvent -> createCategory(event.title)

            is HomeEvent.ShowCategoryMenuEvent -> state.showCategoryMenu = true
            is HomeEvent.SetCategoryEvent -> {
                state.editionDeletionCategory = event.category
            }

            is HomeEvent.EditCategoryEvent -> {
                state.isEditMode = true
                state.showAddEditCategoryDialog = true
            }

            is HomeEvent.DeleteCategoryEvent -> deleteCategory()
            is HomeEvent.HideCategoryMenuEvent -> state.showCategoryMenu = false
            is HomeEvent.SaveEditedCategoryEvent -> saveEditedCategory(event.category)
            is HomeEvent.SubtaskItemCheckboxToggleEvent -> subtaskItemToggle(event.subtask)

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
                state.showTaskPriority = sharedPrefsHelper.showTaskPriority
                state.showTasksStatistics = sharedPrefsHelper.showTasksStatistics

                state.defaultAllCategoriesValue = Category(
                    id = 0, title = getLocaleStringResource(
                        application.resources.configuration.locale,
                        R.string.all_categories,
                        application as Context
                    )
                )
                loadTasks()
                loadCategories()

                defineCompletedTasks()

                state.contentState.isLoading.value = false
            }
        }
    }

    private suspend fun loadTasks() {
        state.tasks = tasksUseCase.getTasksByTimeTypeIdDateAndCategoryIdOperator.invoke(
            timeTypeId = state.timeType.id,
            dateInMilliseconds = when (state.timeType.toTimeTypeEvent()) {
                TimeTypeEnum.DAY -> state.selectedDate.time
                TimeTypeEnum.WEEK -> 0
                TimeTypeEnum.MONTH -> state.selectedMonth.time
                TimeTypeEnum.YEAR -> state.selectedYear.time
                TimeTypeEnum.LIFE -> 0
            },
            dates = Pair(state.selectedWeekStart, state.selectedWeekEnd),
            categoryId = state.selectedCategory.id
        ).map { task -> task.toDomain() }.toMutableList()

        state.tasks.forEachIndexed { index, task ->
            state.tasks[index].subtasks = subtasksUseCase.getSubtasksByTaskIdOperator(task.id)
                .map { subtask -> subtask.toDomain() }.toMutableList()
        }

        if (state.showTaskPriority) {
            state.urgentAndImportantTasks =
                state.tasks.filter { it -> it.priority == Priority.URGENT_AND_IMPORTANT }
                    .toMutableList()
            state.notUrgentAndImportantTasks =
                state.tasks.filter { it -> it.priority == Priority.NOT_URGENT_AND_IMPORTANT }
                    .toMutableList()
            state.urgentAndUnimportantTasks =
                state.tasks.filter { it -> it.priority == Priority.URGENT_AND_UNIMPORTANT }
                    .toMutableList()
            state.notUrgentAndUnimportantTasks =
                state.tasks.filter { it -> it.priority == Priority.NOT_URGENT_AND_UNIMPORTANT }
                    .toMutableList()
            state.notPriorityTasks =
                state.tasks.filter { it -> it.priority == Priority.NOT_PRIORITY }.toMutableList()
        }
    }

    private suspend fun loadCategories() {
        state.categories =
            categoriesUseCase.getCategoriesOperator.invoke().map { category -> category.toDomain() }
                .toMutableList()

        state.selectedCategory =
            state.categories.find { category -> category.id == sharedPrefsHelper.categoryId }
                ?: state.defaultAllCategoriesValue
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun saveEditedCategory(category: Category) {
        state.contentState.isLoading.value = true
        state.showAddEditCategoryDialog = false

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                categoriesUseCase.updateCategoryOperator.invoke(category.toData())
                state.categories.find { categoryItem -> categoryItem.id == category.id }?.title =
                    category.title

                state.isEditMode = false

                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun deleteCategory() {
        state.contentState.isLoading.value = true
        state.showQuestionDialog = false

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                categoriesUseCase.deleteCategoryOperator.invoke(state.editionDeletionCategory.toData())
                state.categories.remove(state.editionDeletionCategory)

                if (state.editionDeletionCategory == state.selectedCategory) {
                    state.selectedCategory = state.defaultAllCategoriesValue
                    loadData()
                }

                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun createCategory(title: String) {
        state.contentState.isLoading.value = true
        state.showAddEditCategoryDialog = false

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                categoriesUseCase.insertCategoryOperator.invoke(Category(title = title).toData())
                loadCategories()

                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun selectCategory(event: HomeEvent.SelectCategoryEvent) {
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            state.selectedCategory = event.category
            sharedPrefsHelper.categoryId = event.category.id
            runBlocking {
                loadData()
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

        val workManager = WorkManager.getInstance(application)

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                for (i in 0..<state.deletionTasks.size) {
                    val notification =
                        notificationsUseCase.getNotificationByTaskId(state.deletionTasks[i].id)
                    var notificationDomain: Notification = Notification()
                    if (notification != null) {
                        notificationDomain = notification.toDomain()
                        workManager.cancelAllWorkByTag(notificationDomain.tag)
                        notificationsUseCase.deleteNotificationOperator.invoke(
                            notification
                        )
                    }

                    tasksUseCase.deleteTaskOperator.invoke(state.deletionTasks[i].toData())
                    state.tasks.remove(state.deletionTasks[i])

                    if (state.showTaskPriority) {
                        state.urgentAndImportantTasks.removeIf { it -> state.deletionTasks[i].id == it.id }
                        state.urgentAndUnimportantTasks.removeIf { it -> state.deletionTasks[i].id == it.id }
                        state.notUrgentAndImportantTasks.removeIf { it -> state.deletionTasks[i].id == it.id }
                        state.notUrgentAndUnimportantTasks.removeIf { it -> state.deletionTasks[i].id == it.id }
                        state.notPriorityTasks.removeIf { it -> state.deletionTasks[i].id == it.id }
                    }

                    tasksUseCase.deleteTaskOperator.invoke(state.deletionTasks[i].toData())
                }

                defineCompletedTasks()

                state.isDeletionMode = false
                state.showQuestionDialog = false
                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun changeDate(event: HomeEvent.SelectDatePickerDialogEvent) {
        state.contentState.isLoading.value = true

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
        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                loadData()
            }
        }
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
                    checked = event.task.checked
                ).toData()
            )
            defineCompletedTasks()
        }
        event.task.checked = !event.task.checked
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun subtaskItemToggle(subtask: Subtask) {
        GlobalScope.launch(Dispatchers.IO) {
            subtasksUseCase.updateSubtaskOperator.invoke(
                subtask.copy(
                    checked = mutableStateOf(
                        subtask.checked.value
                    )
                ).toData()
            )
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun fillDaysToDateList() {
        state.contentState.isLoading.value = true

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

                state.contentState.isLoading.value = false
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

    private fun defineCompletedTasks() {
        state.allCompletedTasks = tasksUseCase.getAllCompletedTasksCount()
        state.allNotCompletedTasks = tasksUseCase.getAllNotCompletedTasksCount()
        state.completedTasks = state.tasks.count { it -> it.checked }
        state.notCompletedTasks = state.tasks.count { it -> !it.checked }
    }
}