package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val sharedPrefsHelper: SharedPrefsHelper) :
    ViewModel() {
    val state by mutableStateOf(SettingsState())

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.LoadDataEvent -> loadData()

            is SettingsEvent.ShowUserNameChangeDialogEvent -> state.showUserNameChangeDialog = true
            is SettingsEvent.HideUserNameChangeDialogEvent -> state.showUserNameChangeDialog = false
            is SettingsEvent.UserNameChangeEvent -> {
                sharedPrefsHelper.name = event.userName
                state.userName = event.userName

                state.showUserNameChangeDialog = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                state.userName = sharedPrefsHelper.name ?: ""

                state.contentState.isLoading.value = false
            }
        }
    }
}