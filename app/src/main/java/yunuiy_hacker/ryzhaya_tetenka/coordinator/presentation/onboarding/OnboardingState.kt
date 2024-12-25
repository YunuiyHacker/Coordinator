package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class OnboardingState {
    var name by mutableStateOf("")

    var succes by mutableStateOf(false)
}