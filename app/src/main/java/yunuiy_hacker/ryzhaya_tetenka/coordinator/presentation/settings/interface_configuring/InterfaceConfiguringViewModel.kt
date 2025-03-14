package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.interface_configuring

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.getValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper

@HiltViewModel
class InterfaceConfiguringViewModel @Inject constructor(private val sharedPrefsHelper: SharedPrefsHelper) :
    ViewModel() {
    val state by mutableStateOf(InterfaceConfiguringState())

    fun onEvent(event: InterfaceConfiguringEvent) {
        when (event) {
            is InterfaceConfiguringEvent.LoadDataEvent -> loadData()

            is InterfaceConfiguringEvent.ToggleTaskPriorityShowEvent -> {
                state.showTaskPriority =
                    !state.showTaskPriority
                sharedPrefsHelper.showTaskPriority = state.showTaskPriority
            }

            is InterfaceConfiguringEvent.ToggleTasksStatisticsShowEvent -> {
                state.showTasksStatistics = !state.showTasksStatistics
                sharedPrefsHelper.showTasksStatistics = state.showTasksStatistics
            }
        }
    }

    private fun loadData() {
        state.showTaskPriority = sharedPrefsHelper.showTaskPriority
        state.showTasksStatistics = sharedPrefsHelper.showTasksStatistics
    }
}