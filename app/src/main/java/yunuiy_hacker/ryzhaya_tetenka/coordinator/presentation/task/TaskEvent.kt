package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.task

import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask

sealed class TaskEvent {
    data object LoadDataEvent : TaskEvent()

    data object ShowTaskMenuEvent : TaskEvent()
    data object HideTaskMenuEvent : TaskEvent()

    data object DeleteTaskEvent : TaskEvent()
    data class ShowQuestionDialogEvent(val title: String, val text: String) : TaskEvent()
    data object HideQuestionDialogEvent : TaskEvent()

    data object OnOpenPlaceInMapModeEvent : TaskEvent()
    data object OpenPlaceInMapEvent : TaskEvent()
    data object OffOpenPlaceInMapModeEvent : TaskEvent()

    data class SubtaskItemCheckboxToggleEvent(val subtask: Subtask) : TaskEvent()
}