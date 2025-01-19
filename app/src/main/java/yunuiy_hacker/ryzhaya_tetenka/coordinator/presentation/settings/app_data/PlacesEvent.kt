package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.app_data

import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place

sealed class PlacesEvent {
    data object LoadDataEvent : PlacesEvent()
    data object ShowPlaceCreateUpdateDialogEvent : PlacesEvent()
    data class CreateOrUpdatePlaceEvent(val place: Place) : PlacesEvent()
    data object HidePlaceCreateUpdateDialogEvent : PlacesEvent()

    data class SelectPlaceEvent(val place: Place) : PlacesEvent()
    data object DeletePlaceEvent : PlacesEvent()
    data class ShowQuestionDialogEvent(val title: String, val text: String) : PlacesEvent()
    data object HideQuestionDialog : PlacesEvent()
}