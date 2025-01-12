package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.state.ContentState

class SettingsState {
    var userName by mutableStateOf("")

    var showUserNameChangeDialog by mutableStateOf(false)

    var contentState by mutableStateOf(ContentState())
}