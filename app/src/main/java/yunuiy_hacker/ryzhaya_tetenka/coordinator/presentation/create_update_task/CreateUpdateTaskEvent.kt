package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_update_task

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

    data object ShowTimeTypePickerMenuEvent : CreateUpdateTaskEvent()
    data class SelectTimeTypePickerMenuEvent(val timeType: TimeType) : CreateUpdateTaskEvent()
    data object HideTimeTypePickerMenuEvent : CreateUpdateTaskEvent()

    data object OnClickButtonEvent : CreateUpdateTaskEvent()
}