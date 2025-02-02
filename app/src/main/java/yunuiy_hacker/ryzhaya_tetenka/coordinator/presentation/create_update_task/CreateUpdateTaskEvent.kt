package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_update_task

import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType

sealed class CreateUpdateTaskEvent {
    data object LoadDataEvent : CreateUpdateTaskEvent()

    data class HeadingChangeEvent(val heading: String) : CreateUpdateTaskEvent()
    data class ContentChangeEvent(val content: String) : CreateUpdateTaskEvent()

    data object ShowDatePickerDialogEvent : CreateUpdateTaskEvent()
    data class SelectDatePickerDialogEvent(val dateInMilliseconds: Long) : CreateUpdateTaskEvent()
    data object HideDatePickerDialogEvent : CreateUpdateTaskEvent()

    data object ShowTimePickerDialogEvent : CreateUpdateTaskEvent()
    data class SelectTimePickerDialogEvent(val hour: Int, val minute: Int) : CreateUpdateTaskEvent()
    data object HideTimePickerDialogEvent : CreateUpdateTaskEvent()

    data object ShowEndTimePickerDialogEvent : CreateUpdateTaskEvent()
    data class SelectEndTimePickerDialogEvent(val hour: Int, val minute: Int) :
        CreateUpdateTaskEvent()

    data object HideEndTimePickerDialogEvent : CreateUpdateTaskEvent()

    data object ShowTimeTypePickerMenuEvent : CreateUpdateTaskEvent()
    data class SelectTimeTypePickerMenuEvent(val timeType: TimeType) : CreateUpdateTaskEvent()
    data object HideTimeTypePickerMenuEvent : CreateUpdateTaskEvent()

    data object ShowCategorySelectorMenuEvent : CreateUpdateTaskEvent()
    data class SelectCategoryMenuEvent(val category: Category) : CreateUpdateTaskEvent()
    data object HideCategorySelectorMenuEvent : CreateUpdateTaskEvent()

    data object ShowTaskMenuEvent : CreateUpdateTaskEvent()
    data object HideTaskMenuEvent : CreateUpdateTaskEvent()

    data object ShowMessageDialogEvent : CreateUpdateTaskEvent()
    data object HideMessageDialogEvent : CreateUpdateTaskEvent()

    data object EndTimeCheckToggleEvent : CreateUpdateTaskEvent()

    data object AddSubtaskEvent : CreateUpdateTaskEvent()
    data class DeleteSubtaskEvent(val subtask: Subtask) : CreateUpdateTaskEvent()

    data object ShowPlaceSelectorSheetEvent : CreateUpdateTaskEvent()
    data class SelectPlaceEvent(val place: Place) : CreateUpdateTaskEvent()
    data object HidePlaceSelectorSheetEvent : CreateUpdateTaskEvent()

    data object ShowPlaceCreateUpdateDialogEvent : CreateUpdateTaskEvent()
    data class CreatePlaceEvent(val place: Place) : CreateUpdateTaskEvent()
    data object HidePlaceCreateUpdateDialogEvent : CreateUpdateTaskEvent()

    data object ShowPeopleSelectorSheetEvent : CreateUpdateTaskEvent()
    data class AddPeopleEvent(val people: People) : CreateUpdateTaskEvent()
    data object HidePeopleSelectorSheetEvent : CreateUpdateTaskEvent()

    data object OnBackPressEvent : CreateUpdateTaskEvent()

    data object OnClickButtonEvent : CreateUpdateTaskEvent()
}