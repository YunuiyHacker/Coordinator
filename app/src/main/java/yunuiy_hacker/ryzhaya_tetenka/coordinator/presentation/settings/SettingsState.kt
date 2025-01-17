package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.state.ContentState

class SettingsState {
    var userName by mutableStateOf("")

    var selectedFileUri by mutableStateOf(Uri.parse(""))

    var questionTitle by mutableStateOf("")
    var questionText by mutableStateOf("")

    var showUserNameChangeDialog by mutableStateOf(false)
    var showMessageDialog by mutableStateOf(false)
    var showQuestionDialog by mutableStateOf(false)

    var contentState by mutableStateOf(ContentState())
}