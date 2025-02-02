package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.fill_name

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class FillNameState {
    var name by mutableStateOf("")

    var succes by mutableStateOf(false)
}