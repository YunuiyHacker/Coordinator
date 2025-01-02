package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph

sealed class Route(val route: String) {
    data object HomeScreen : Route("homeScreen")
    data object OnboardingScreen : Route("onboardingScreen")
    data object CreateTaskScreen: Route("createTaskScreen")
}