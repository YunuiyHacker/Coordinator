package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.app_data.people

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.widget.Toast
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
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples.PeoplesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case.SaveImageUseCase
import javax.inject.Inject

@HiltViewModel
class PeoplesViewModel @Inject constructor(
    private val peoplesUseCase: PeoplesUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val application: Application
) : ViewModel() {
    val state by mutableStateOf(PeoplesState())

    fun onEvent(event: PeoplesEvent) {
        when (event) {
            is PeoplesEvent.LoadDataEvent -> loadData()
            is PeoplesEvent.ShowCreateUpdatePeopleBottomSheetEvent -> {
                state.selectedPeople.value = event.people
                state.isOnlyReadPeople = event.isReadOnly
                state.showCreateUpdatePeopleBottomSheet = true
            }

            is PeoplesEvent.HideCreateUpdatePeopleBottomSheetEvent -> {
                state.showCreateUpdatePeopleBottomSheet =
                    false
                state.isEditMode = false
            }

            is PeoplesEvent.CreateUpdatePeopleEvent -> createUpdatePeople(
                event.people, event.imageUri
            )

            is PeoplesEvent.ShowQuestionDialogEvent -> {
                state.showQuestionDialog = true
                state.questionTitle = event.title
                state.questionText = event.text
            }

            is PeoplesEvent.HideQuestionDialogEvent -> state.showQuestionDialog = false

            is PeoplesEvent.ShowImageEvent -> state.showPhoto = true
            is PeoplesEvent.HideImageEvent -> state.showPhoto = false

            is PeoplesEvent.DeletePeopleEvent -> deletePeople()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                state.peoples =
                    peoplesUseCase.getPeoplesOperator.invoke().map { people -> people.toDomain() }
                        .toMutableList()

                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun createUpdatePeople(people: People, imageUri: Uri) {
        state.contentState.isLoading.value = true
        state.canCloseCreateUpdatePeopleButtonSheet = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                var people = people
                state.selectedImageUri = imageUri
                if (!state.selectedImageUri.path.isNullOrEmpty()) {
                    val source = ImageDecoder.createSource(
                        application.contentResolver, state.selectedImageUri
                    )
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    people = people.copy(avatarPath = saveImageUseCase.invoke(bitmap))
                }
                if (!state.isEditMode && !state.isOnlyReadPeople) peoplesUseCase.insertPeopleOperator(
                    people.toData()
                )
                else peoplesUseCase.updatePeopleOperator(people.toData())
                loadData()

                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun deletePeople() {
        state.contentState.isLoading.value = true
        state.showQuestionDialog = false
        state.canCloseCreateUpdatePeopleButtonSheet = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                peoplesUseCase.deletePeopleOperator(state.selectedPeople.value?.toData()!!)
                state.peoples.remove(state.selectedPeople.value)

                state.contentState.isLoading.value = false
            }
        }
    }
}
