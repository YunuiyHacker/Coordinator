package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.fill_name

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import javax.inject.Inject

@HiltViewModel
class FillNameViewModel @Inject constructor(private val sharedPrefsHelper: SharedPrefsHelper) :
    ViewModel() {
    val state by mutableStateOf(FillNameState())

    fun onEvent(event: FillNameEvent) {
        when (event) {
            is FillNameEvent.ChangeNameEvent -> state.name = event.name
            is FillNameEvent.OnClickButton -> saveName()
        }
    }

    private fun saveName() {
        sharedPrefsHelper.name = state.name

        state.succes = state.name.length >= 2
    }
}