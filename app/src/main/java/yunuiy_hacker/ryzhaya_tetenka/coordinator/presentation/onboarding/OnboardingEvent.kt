package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding

sealed class OnboardingEvent {
    data class ChangeNameEvent(val name: String) : OnboardingEvent()
    data object OnClickButton : OnboardingEvent()
}