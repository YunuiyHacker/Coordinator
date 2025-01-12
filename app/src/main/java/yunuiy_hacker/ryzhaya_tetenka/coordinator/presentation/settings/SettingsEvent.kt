package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings

sealed class SettingsEvent {
    data object LoadDataEvent : SettingsEvent()

    data object ShowUserNameChangeDialogEvent : SettingsEvent()
    data object HideUserNameChangeDialogEvent : SettingsEvent()
    data class UserNameChangeEvent(val userName: String) : SettingsEvent()
}