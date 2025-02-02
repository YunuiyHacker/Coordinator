package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.app_data.people

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.state.ContentState

class PeoplesState {
    var peoples: MutableList<People> = mutableListOf()
    var selectedPeople: MutableState<People?> = mutableStateOf(null)

    var showCreateUpdatePeopleBottomSheet by mutableStateOf(false)

    var selectedImageUri by mutableStateOf(Uri.parse(""))

    var isEditMode by mutableStateOf(false)
    var isOnlyReadPeople by mutableStateOf(false)

    var questionTitle by mutableStateOf("")
    var questionText by mutableStateOf("")
    var showQuestionDialog by mutableStateOf(false)

    var canCloseCreateUpdatePeopleButtonSheet by mutableStateOf(false)

    var showPhoto by mutableStateOf(false)

    val contentState by mutableStateOf(ContentState())
}