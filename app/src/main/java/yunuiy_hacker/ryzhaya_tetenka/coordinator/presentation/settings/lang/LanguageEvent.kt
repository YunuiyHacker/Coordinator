package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.lang

import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.model.Language

sealed class LanguageEvent {
    data object LoadDataEvent : LanguageEvent()

    data class ChangeLanguageEvent(val language: Language) : LanguageEvent()

    data class ShowMessageWithButtonDialog(val message: String) : LanguageEvent()
    data object HideMessageWithButtonDialog : LanguageEvent()
}