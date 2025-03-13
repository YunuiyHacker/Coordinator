package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.fill_name

import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.model.Language

sealed class FillNameEvent {
    data object LoadDataEvent : FillNameEvent()

    data class ChangeNameEvent(val name: String) : FillNameEvent()

    data class ChangeLanguageEvent(val language: Language) : FillNameEvent()
    data object ToggleThemeEvent : FillNameEvent()

    data object OnClickButton : FillNameEvent()
}