package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph

sealed class Route(val route: String) {
    data object HomeScreen : Route("homeScreen")
    data object FillNameScreen : Route("fillNameScreen")
    data object OnboardingScreen : Route("onboardingScreen")
    data object CreateUpdateTaskScreen : Route("createUpdateTaskScreen")
    data object TaskScreen : Route("taskScreen")
    data object SettingsScreen : Route("settingsScreen")
    data object PlacesScreen : Route("placesScreen")
    data object PeoplesScreen : Route("peoplesScreen")
    data object DeveloperScreen : Route("developerScreen")
    data object LanguageScreen : Route("languageScreen")
    data object InterfaceConfiguringScreen : Route("interfaceConfiguringScreen")
}