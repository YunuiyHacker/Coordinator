package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.app_data.people

import android.net.Uri
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People

sealed class PeoplesEvent {
    data object LoadDataEvent : PeoplesEvent()

    data class ShowCreateUpdatePeopleBottomSheetEvent(
        val people: People?,
        val isReadOnly: Boolean
    ) : PeoplesEvent()

    data object HideCreateUpdatePeopleBottomSheetEvent : PeoplesEvent()
    data class CreateUpdatePeopleEvent(val people: People, val imageUri: Uri) : PeoplesEvent()

    data class ShowQuestionDialogEvent(val title: String, val text: String) : PeoplesEvent()
    data object HideQuestionDialogEvent : PeoplesEvent()

    data object ShowImageEvent : PeoplesEvent()
    data object HideImageEvent : PeoplesEvent()

    data object DeletePeopleEvent : PeoplesEvent()
}