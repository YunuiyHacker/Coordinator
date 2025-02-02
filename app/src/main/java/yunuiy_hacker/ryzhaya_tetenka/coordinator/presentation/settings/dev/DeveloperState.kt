package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.dev

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class DeveloperState {
    var showDeveloperDialog by mutableStateOf(false)
    var phoneCopied by mutableStateOf(false)
}