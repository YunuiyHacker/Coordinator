package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val sharedPrefsHelper: SharedPrefsHelper) :
    ViewModel() {
    val state by mutableStateOf(OnboardingState())

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.ChangeNameEvent -> state.name = event.name
            is OnboardingEvent.OnClickButton -> saveName()
        }
    }

    private fun saveName() {
        sharedPrefsHelper.name = state.name

        state.succes = state.name.length >= 2
    }
}