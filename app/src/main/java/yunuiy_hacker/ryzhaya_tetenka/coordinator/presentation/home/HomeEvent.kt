package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home

import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType

sealed class HomeEvent {
    data class TimeTypeChangeEvent(val timeType: TimeType) : HomeEvent()
    data class SearchQueryChangeEvent(val query: String) : HomeEvent()

    data object OnClickAddNewTaskEvent : HomeEvent()
}