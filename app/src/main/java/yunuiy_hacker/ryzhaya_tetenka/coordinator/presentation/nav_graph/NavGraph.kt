package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_task.CreateTaskScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_task.CreateTaskViewModel
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.HomeScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding.OnboardingScreen
import java.util.Date

@Composable
fun NavGraph(
    navHostController: NavHostController, startDestination: String = Route.HomeScreen.route
) {
    NavHost(
        navController = navHostController, startDestination = startDestination
    ) {
        composable(route = Route.HomeScreen.route) {
            HomeScreen(navHostController)
        }
        composable(route = Route.OnboardingScreen.route) {
            OnboardingScreen(navHostController)
        }
        composable(
            route = "${Route.CreateTaskScreen.route}/{time_type_id}/{date_in_long}/{week_date_first_part_in_milliseconds}/{week_date_second_part_in_milliseconds}",
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                )
            },
            arguments = listOf(navArgument("time_type_id") {
                NavType.StringType
                nullable = false
            }, navArgument("date_in_long") {
                NavType.StringType
                nullable = false
            }, navArgument("week_date_first_part_in_milliseconds") {
                NavType.StringType
                nullable = false
            }, navArgument("week_date_second_part_in_milliseconds") {
                NavType.StringType
                nullable = false
            })
        ) {
            val timeTypeId = it.arguments?.getString("time_type_id")!!.toInt()
            val dateInMilliseconds = it.arguments?.getString("date_in_long")!!.toLong()
            val weekDateFirstPartInMilliseconds =
                it.arguments?.getString("week_date_first_part_in_milliseconds")!!.toLong()
            val weekDateSecondPartInMilliseconds =
                it.arguments?.getString("week_date_second_part_in_milliseconds")!!.toLong()

            val viewModel: CreateTaskViewModel = hiltViewModel()
            viewModel.state.timeTypeId = timeTypeId
            viewModel.state.dateInMilliseconds = dateInMilliseconds
            viewModel.state.weekDate =
                Pair(Date(weekDateFirstPartInMilliseconds), Date(weekDateSecondPartInMilliseconds))

            CreateTaskScreen(
                navHostController = navHostController, viewModel = viewModel
            )
        }
    }
}