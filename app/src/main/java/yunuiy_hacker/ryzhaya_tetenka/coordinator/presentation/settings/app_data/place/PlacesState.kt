package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.app_data.place

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.state.ContentState

class PlacesState {
    var places: MutableList<Place> = mutableListOf()

    var selectedPlace: MutableState<Place?> = mutableStateOf(null)
    var showCreateUpdatePlaceDialog by mutableStateOf(false)

    var selectedOperation by mutableStateOf(PlaceOperationsEnum.DELETE)
    var showQuestionDialog by mutableStateOf(false)
    var questionTitle by mutableStateOf("")
    var questionText by mutableStateOf("")

    var contentState by mutableStateOf(ContentState())
}