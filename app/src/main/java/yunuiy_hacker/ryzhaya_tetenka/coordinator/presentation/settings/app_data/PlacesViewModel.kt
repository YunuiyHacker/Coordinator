package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.app_data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toData
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers.toDomain
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.PlacesUseCase
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(private val placesUseCase: PlacesUseCase) : ViewModel() {
    val state by mutableStateOf(PlacesState())

    fun onEvent(event: PlacesEvent) {
        when (event) {
            is PlacesEvent.LoadDataEvent -> loadData()
            is PlacesEvent.ShowPlaceCreateUpdateDialogEvent -> state.showCreateUpdatePlaceDialog =
                true

            is PlacesEvent.CreateOrUpdatePlaceEvent -> createOrUpdatePlace(event.place)

            is PlacesEvent.HidePlaceCreateUpdateDialogEvent -> state.showCreateUpdatePlaceDialog =
                false

            is PlacesEvent.SelectPlaceEvent -> state.selectedPlace = event.place
            is PlacesEvent.DeletePlaceEvent -> deletePlace()
            is PlacesEvent.ShowQuestionDialogEvent -> {
                state.showQuestionDialog = true
                state.questionTitle = event.title
                state.questionText = event.text
            }

            is PlacesEvent.HideQuestionDialog -> state.showQuestionDialog = false
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                state.places = placesUseCase.getPlacesOperator().map { place ->
                    place.toDomain()
                }.toMutableList()

                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun createOrUpdatePlace(place: Place) {
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                if (state.isEditMode) placesUseCase.updatePlaceOperator(place.toData())
                else placesUseCase.insertPlaceOperator(place.toData())
                loadData()
                state.showCreateUpdatePlaceDialog = false

                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun deletePlace() {
        state.showQuestionDialog = false
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                placesUseCase.deletePlaceOperator(state.selectedPlace.toData())
                state.places.remove(state.selectedPlace)

                state.contentState.isLoading.value = false
            }
        }
    }
}