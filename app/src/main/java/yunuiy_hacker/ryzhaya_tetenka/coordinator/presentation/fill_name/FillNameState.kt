package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.fill_name

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.model.Language

class FillNameState {
    var name by mutableStateOf("")

    var isDarkTheme by mutableStateOf(false)
    var language by mutableStateOf(Language())

    var succes by mutableStateOf(false)
}