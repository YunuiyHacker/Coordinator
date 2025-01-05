package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.task

sealed class TaskEvent {
    data object LoadDataEvent : TaskEvent()

    data object ShowTaskMenuEvent : TaskEvent()
    data object HideTaskMenuEvent : TaskEvent()

    data object DeleteTaskEvent : TaskEvent()
    data object ShowQuestionDialogEvent : TaskEvent()
    data object HideQuestionDialogEvent : TaskEvent()
}