package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.dev

sealed class DeveloperEvent {
    data object ShowDeveloperEvent : DeveloperEvent()
    data object HideDeveloperEvent : DeveloperEvent()

    data object CopyPhoneEvent : DeveloperEvent()
    data object ResetCopiedPhoneEvent : DeveloperEvent()
}