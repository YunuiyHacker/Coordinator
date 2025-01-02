package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_task

import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType

sealed class CreateTaskEvent {
    data object InitEvent : CreateTaskEvent()

    data class HeadingChangeEvent(val heading: String) : CreateTaskEvent()
    data class ContentChangeEvent(val content: String) : CreateTaskEvent()

    data object ShowDatePickerDialogEvent : CreateTaskEvent()
    data class SelectDatePickerDialogEvent(val dateInMilliseconds: Long) : CreateTaskEvent()
    data object HideDatePickerDialogEvent : CreateTaskEvent()

    data object ShowTimePickerDialogEvent : CreateTaskEvent()
    data class SelectTimePickerDialogEvent(val hour: Int, val minute: Int) : CreateTaskEvent()
    data object HideTimePickerDialogEvent : CreateTaskEvent()

    data object ShowTimeTypePickerMenuEvent : CreateTaskEvent()
    data class SelectTimeTypePickerMenuEvent(val timeType: TimeType) : CreateTaskEvent()
    data object HideTimeTypePickerMenuEvent : CreateTaskEvent()

    data object OnClickButtonEvent : CreateTaskEvent()
}