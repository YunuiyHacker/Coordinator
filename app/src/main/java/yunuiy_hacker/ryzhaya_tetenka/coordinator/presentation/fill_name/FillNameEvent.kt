package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.fill_name

sealed class FillNameEvent {
    data class ChangeNameEvent(val name: String) : FillNameEvent()
    data object OnClickButton : FillNameEvent()
}