package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.interface_configuring

sealed class InterfaceConfiguringEvent {
    data object LoadDataEvent : InterfaceConfiguringEvent()

    data object ToggleTaskPriorityShowEvent : InterfaceConfiguringEvent()
    data object ToggleTasksStatisticsShowEvent : InterfaceConfiguringEvent()

}