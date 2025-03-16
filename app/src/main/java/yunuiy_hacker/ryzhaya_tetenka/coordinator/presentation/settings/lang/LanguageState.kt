package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.lang

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.model.Language
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.state.ContentState

class LanguageState {
    var language by mutableStateOf(Language())

    val contentState by mutableStateOf(ContentState())

    var message by mutableStateOf("")
    var showMessageWithButtonDialog by mutableStateOf(false)
}