package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.interface_configuring

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class InterfaceConfiguringState {
    var showTaskPriority by mutableStateOf(true)
    var showTasksStatistics by mutableStateOf(true)
}