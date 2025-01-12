package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home

import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType

sealed class HomeEvent {
    data object LoadDataEvent : HomeEvent()

    data object SelectedDateChangeEvent : HomeEvent()

    data object ShowDatePickerDialogEvent : HomeEvent()
    data class SelectDatePickerDialogEvent(val dateInMilliseconds: Long) : HomeEvent()
    data object HideDatePickerDialogEvent : HomeEvent()

    data class TimeTypeChangeEvent(val timeType: TimeType) : HomeEvent()
    data class SearchQueryChangeEvent(val query: String) : HomeEvent()
    data object OnClickSearchEvent : HomeEvent()

    data object OnDeletionModeEvent : HomeEvent()
    data object OffDeletionModeEvent : HomeEvent()

    data class ShowQuestionDialogEvent(val title: String, val text: String) : HomeEvent()
    data object HideQuestionDialogEvent : HomeEvent()
    data object SelectAllEvent : HomeEvent()
    data object UnselectAllEvent : HomeEvent()
    data class AddSelectedTaskEvent(val task: Task) : HomeEvent()
    data class RemoveSelectedTaskEvent(val task: Task) : HomeEvent()
    data object DeleteAllSelectedTasksEvent : HomeEvent()

    data class TaskItemCheckboxToggleEvent(val task: Task) : HomeEvent()

    data class SelectCategoryEvent(val category: Category) : HomeEvent()

    data object ShowAddCategoryDialogEvent : HomeEvent()
    data class CreateCategoryEvent(val title: String) : HomeEvent()
    data object HideAddCategoryDialogEvent : HomeEvent()

    data object ShowCategoryMenuEvent : HomeEvent()
    data class SetCategoryEvent(val category: Category) : HomeEvent()
    data object EditCategoryEvent : HomeEvent()
    data object DeleteCategoryEvent : HomeEvent()
    data object HideCategoryMenuEvent : HomeEvent()
    data class SaveEditedCategoryEvent(val category: Category) : HomeEvent()
    data class SubtaskItemCheckboxToggleEvent(val subtask: Subtask) : HomeEvent()

    data object OnClickAddNewTaskEvent : HomeEvent()
}