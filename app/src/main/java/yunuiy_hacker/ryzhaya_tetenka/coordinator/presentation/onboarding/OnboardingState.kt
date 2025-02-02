package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.state.ContentState

class OnboardingState {
    var tasks: MutableList<Task> = mutableListOf()
    var place by mutableStateOf(Place())
    var peoples: MutableList<People> = mutableListOf()

    val contentState by mutableStateOf(ContentState())
}