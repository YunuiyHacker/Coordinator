package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding

sealed class OnboardingEvent {
    data object LoadDataEvent : OnboardingEvent()
}