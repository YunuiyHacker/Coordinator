package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.app_data.place

import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place

sealed class PlacesEvent {
    data object LoadDataEvent : PlacesEvent()
    data object ShowPlaceCreateUpdateDialogEvent : PlacesEvent()
    data class CreateOrUpdatePlaceEvent(val place: Place) : PlacesEvent()
    data object HidePlaceCreateUpdateDialogEvent : PlacesEvent()

    data class PreExecutionOperationWithPlaceEvent(
        val place: Place,
        val title: String,
        val text: String,
        val operation: PlaceOperationsEnum
    ) : PlacesEvent()

    data object DeletePlaceEvent : PlacesEvent()
    data object OpenInMapPlaceEvent : PlacesEvent()
    data object HideQuestionDialog : PlacesEvent()
}