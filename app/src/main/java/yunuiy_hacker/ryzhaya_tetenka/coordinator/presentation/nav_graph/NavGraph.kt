package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.HomeScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding.OnboardingScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    startDestination: String = Route.HomeScreen.route
) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable(route = Route.HomeScreen.route) {
            HomeScreen(navHostController)
        }
        composable(route = Route.OnboardingScreen.route) {
            OnboardingScreen(navHostController)
        }
    }
}