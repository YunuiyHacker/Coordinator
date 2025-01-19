package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.app_data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.state.ContentState

class PlacesState {
    var places: MutableList<Place> = mutableListOf()

    var isEditMode by mutableStateOf(false)
    var selectedPlace by mutableStateOf(Place())
    var showCreateUpdatePlaceDialog by mutableStateOf(false)

    var showQuestionDialog by mutableStateOf(false)
    var questionTitle by mutableStateOf("")
    var questionText by mutableStateOf("")

    var contentState by mutableStateOf(ContentState())
}