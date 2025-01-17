package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings

sealed class SettingsEvent {
    data object LoadDataEvent : SettingsEvent()

    data object ShowUserNameChangeDialogEvent : SettingsEvent()
    data object HideUserNameChangeDialogEvent : SettingsEvent()
    data class UserNameChangeEvent(val userName: String) : SettingsEvent()

    data object ShowMessageDialogEvent : SettingsEvent()
    data object HideMessageDialogEvent : SettingsEvent()

    data class ShowQuestionDialogEvent(val title: String, val text: String) : SettingsEvent()
    data object HideQuestionDialogEvent : SettingsEvent()

    data object ExportDataOnClick : SettingsEvent()
    data object ImportDataOnClick : SettingsEvent()
}