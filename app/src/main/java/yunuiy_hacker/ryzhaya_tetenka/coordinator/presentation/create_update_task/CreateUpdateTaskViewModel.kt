package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_update_task

import android.app.Application
import android.widget.Toast
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
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PlaceInTask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toData
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toDomain
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.PeopleInTask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.CategoriesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples.PeoplesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples_in_tasks.PeoplesInTasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.PlacesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.PlacesInTasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.SubtasksUseCase
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
    private val subtasksUseCase: SubtasksUseCase,
    private val placesUseCase: PlacesUseCase,
    private val placesInTasksUseCase: PlacesInTasksUseCase,
    private val peoplesUseCase: PeoplesUseCase,
    private val peoplesInTasksUseCase: PeoplesInTasksUseCase,
    private val sharedPrefsHelper: SharedPrefsHelper,
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
            is CreateUpdateTaskEvent.HideTimePickerDialogEvent -> {
                state.showTimePickerDialog = false
                state.showEndTimePickerDialog = false
            }

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

            is CreateUpdateTaskEvent.AddSubtaskEvent -> addSubtask()
            is CreateUpdateTaskEvent.DeleteSubtaskEvent -> deleteSubtask(event.subtask)

            is CreateUpdateTaskEvent.ShowPlaceSelectorSheetEvent -> {
                loadPlaces()
                state.showPlacesSelectorSheet = true
            }

            is CreateUpdateTaskEvent.SelectPlaceEvent -> {
                state.showPlacesSelectorSheet = false
                state.selectedPlace = event.place
            }

            is CreateUpdateTaskEvent.HidePlaceSelectorSheetEvent -> state.showPlacesSelectorSheet =
                false

            is CreateUpdateTaskEvent.ShowPlaceCreateUpdateDialogEvent -> state.showCreateUpdatePlaceDialog =
                true

            is CreateUpdateTaskEvent.CreatePlaceEvent -> createPlace(event.place)
            is CreateUpdateTaskEvent.HidePlaceCreateUpdateDialogEvent -> state.showCreateUpdatePlaceDialog =
                false

            is CreateUpdateTaskEvent.ShowPeopleSelectorSheetEvent -> {
                loadPeoples()
                state.showPeoplesSelectorSheet = true
            }

            is CreateUpdateTaskEvent.AddPeopleEvent -> {
                state.showPeoplesSelectorSheet = false
                if (!state.selectedPeoples.contains(event.people)) state.selectedPeoples.add(event.people)
                else Toast.makeText(
                    application,
                    application.getString(R.string.this_people_already_will_selected),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is CreateUpdateTaskEvent.HidePeopleSelectorSheetEvent -> state.showPeoplesSelectorSheet =
                false

            is CreateUpdateTaskEvent.OnBackPressEvent -> saveTaskData()

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
            state.selectedCategory =
                state.categories.find { category -> category.id == state.categoryId }!!
            state.heading = sharedPrefsHelper.unsavedTitle ?: ""
            state.content = sharedPrefsHelper.unsavedContent ?: ""
        }

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                if (state.taskId != 0) {
                    state.task = tasksUseCase.getTaskByIdOperator.invoke(state.taskId).toDomain()

                    state.heading = state.task.title
                    state.content = state.task.content
                    state.checked = state.task.checked
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
                    state.subtasks.addAll(subtasksUseCase.getSubtasksByTaskIdOperator(state.taskId)
                        .map { subtask -> subtask.toDomain() })
                    val dataPlace = placesUseCase.getPlaceByIdOperator(
                        placesInTasksUseCase.getPlacesInTaskByTaskId(state.taskId)?.placeId ?: 0
                    )
                    if (dataPlace != null) state.selectedPlace = dataPlace.toDomain()
                    val peoplesInTask =
                        peoplesInTasksUseCase.getPeoplesInTasksByTaskIdOperator(state.taskId)
                    for (i in 0..<peoplesInTask.size) {
                        val people =
                            peoplesUseCase.getPeopleByIdOperator(peoplesInTask[i].peopleId!!)
                        if (people != null) state.selectedPeoples.add(
                            people.toDomain()
                        )
                    }
                }

                state.contentState.isLoading.value = false
            }
        }
    }

    private fun saveTaskData() {
        if (state.taskId == 0) {
            sharedPrefsHelper.unsavedTitle = state.heading
            sharedPrefsHelper.unsavedContent = state.content
        }
    }

    private fun addSubtask() {
        state.contentState.isLoading.value = true

        state.subtasks.add(
            Subtask(
                id = state.subtasks.size + 1, title = "", checked = mutableStateOf(false)
            )
        )

        state.contentState.isLoading.value = false
    }

    private fun deleteSubtask(subtask: Subtask) {
        state.contentState.isLoading.value = true

        state.subtasks.remove(subtask)

        state.contentState.isLoading.value = false
    }

    private fun changeCategory(event: CreateUpdateTaskEvent.SelectCategoryMenuEvent) {
        state.selectedCategory = event.category

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
        val task = Task(
            id = state.taskId,
            checked = state.checked,
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
                        val taskId: Long = tasksUseCase.insertTaskOperator.invoke(task)
                        createOrUpdateSubtasks(taskId)
                        createOrUpdatePlace(taskId.toInt())
                        createOrRemovePeoplesInTasks(taskId.toInt())
                    } else {
                        tasksUseCase.updateTaskOperator.invoke(task)
                        createOrUpdateSubtasks(task.id.toLong())
                        createOrUpdatePlace(task.id)
                        createOrRemovePeoplesInTasks(task.id)
                    }
                    state.success = true
                }
                clearUnsavedData()
            } else {
                state.contentState.exception.value = Exception(
                    application.getString(R.string.end_time_can_not_be_before_start_time)
                )
                state.showMessageDialog = true
            }
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                if (state.taskId == 0) {
                    val taskId: Long = tasksUseCase.insertTaskOperator.invoke(task)
                    createOrUpdateSubtasks(taskId)
                    createOrUpdatePlace(taskId.toInt())
                    createOrRemovePeoplesInTasks(taskId.toInt())
                } else {
                    tasksUseCase.updateTaskOperator.invoke(task)
                    createOrUpdateSubtasks(task.id.toLong())
                    createOrUpdatePlace(task.id)
                    createOrRemovePeoplesInTasks(task.id)
                }

                state.success = true
            }
            clearUnsavedData()
        }
    }

    private suspend fun createOrUpdateSubtasks(taskId: Long) {
        state.subtasks.removeIf { subtask -> subtask.title.isEmpty() }

        val currentSubtasks: List<Subtask> = state.subtasks
        val databaseSubtasks: List<Subtask> =
            subtasksUseCase.getSubtasksByTaskIdOperator(taskId.toInt())
                .map { subtask -> subtask.toDomain() }

        val currentSubtaskIds = currentSubtasks.map { it.id }.toSet()
        val databaseSubtaskIds = databaseSubtasks.map { it.id }.toSet()

        val subtasksToAdd = currentSubtasks.filter { it.id !in databaseSubtaskIds }
        val subtasksToDelete = databaseSubtasks.filter { it.id !in currentSubtaskIds }
        val subtasksToUpdate = currentSubtasks.filter { subtask ->
            val dbSubtask = databaseSubtasks.find { it.id == subtask.id }
            dbSubtask != null && (dbSubtask.title != subtask.title || dbSubtask.checked.value != subtask.checked.value)
        }

        subtasksUseCase.updateSubtasksOperator(subtasksToUpdate.map { subtask ->
            subtask.toData().copy(taskId = taskId.toInt())
        })
        subtasksUseCase.insertSubtasksOperator(subtasksToAdd.map { subtask ->
            subtask.toData().copy(id = 0, taskId = taskId.toInt())
        })
        subtasksUseCase.deleteSubtasksOperator(subtasksToDelete.map { subtask ->
            subtask.toData().copy(taskId = taskId.toInt())
        })
    }

    private fun clearUnsavedData() {
        sharedPrefsHelper.unsavedTitle = ""
        sharedPrefsHelper.unsavedContent = ""
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadPlaces() {
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            launch {
                state.places = placesUseCase.getPlacesOperator().map { place -> place.toDomain() }
                    .toMutableList()

                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadPeoples() {
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            launch {
                state.peoples =
                    peoplesUseCase.getPeoplesOperator().map { people -> people.toDomain() }
                        .toMutableList()

                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun createPlace(place: Place) {
        state.showCreateUpdatePlaceDialog = false

        GlobalScope.launch(Dispatchers.IO) {
            launch {
                placesUseCase.insertPlaceOperator(place.toData())
                loadPlaces()
            }
        }
    }

    private suspend fun createOrUpdatePlace(taskId: Int) {
        val place = placesInTasksUseCase.getPlacesInTaskByTaskId(taskId)

        if (state.selectedPlace.id != 0) {
            if (place == null) placesInTasksUseCase.insertPlaceInTaskOperator(
                placeInTask = PlaceInTask(
                    taskId = taskId, placeId = state.selectedPlace.id
                )
            )
            else placesInTasksUseCase.updatePlaceInTaskOperator(
                placeInTask = place.copy(placeId = state.selectedPlace.id)
            )
        } else {
            if (place != null)
                placesInTasksUseCase.deletePlaceInTaskOperator(
                    placeInTask = place.copy(placeId = state.selectedPlace.id)
                )
        }
    }

    private suspend fun createOrRemovePeoplesInTasks(taskId: Int) {
        var peoplesInTasks: MutableList<PeopleInTask> = mutableListOf()
        if (state.taskId != 0) {
            peoplesInTasks = peoplesInTasksUseCase.getPeoplesInTasksByTaskIdOperator(state.taskId)
                .map { peopleInTask -> peopleInTask.toDomain() }.toMutableList()
            val selectedPeoplesIds = state.selectedPeoples.map { people -> people.id }
            val peoplesInTasksIds = peoplesInTasks.map { peopleInTask -> peopleInTask.peopleId }

            val commonIds = selectedPeoplesIds.intersect(peoplesInTasksIds)
            val onlyPeoplesInTasksIds = peoplesInTasksIds.subtract(selectedPeoplesIds)

            val peoplesInTasksForDeletion = mutableListOf<Int>()

            val peopleInTasksAlreadyExists =
                peoplesInTasks.filter { it.peopleId in commonIds }.map { it.toData() }
            peopleInTasksAlreadyExists.forEach {
                peoplesInTasksForDeletion.add(it.peopleId ?: 0)
            }

            val peopleInTasksForDeletion =
                peoplesInTasks.filter { it.peopleId in onlyPeoplesInTasksIds }.map { it.toData() }
            peopleInTasksForDeletion.forEach {
                peoplesInTasksForDeletion.add(it.peopleId ?: 0)
            }

            val peopleInTasksForAdding =
                state.selectedPeoples.filter { it.id !in peoplesInTasksForDeletion }
            peopleInTasksForAdding.forEach {
            }

            peoplesInTasksUseCase.insertPeoplesInTasksOperator(peopleInTasksForAdding.map { people ->
                PeopleInTask(id = 0, peopleId = people.id, taskId = taskId).toData()
            })

            peoplesInTasksUseCase.deletePeoplesInTasksOperator(peopleInTasksForDeletion.map { peopleInTask -> peopleInTask })
        } else {
            for (i in 0..<state.selectedPeoples.size) {
                peoplesInTasks.add(
                    PeopleInTask(
                        peopleId = state.selectedPeoples[i].id, taskId = taskId
                    )
                )
            }
            peoplesInTasksUseCase.insertPeoplesInTasksOperator(peoplesInTasks.map { peopleInTask -> peopleInTask.toData() })
        }
    }
}