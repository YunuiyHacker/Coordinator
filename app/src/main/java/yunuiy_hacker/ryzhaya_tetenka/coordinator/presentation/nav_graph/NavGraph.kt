package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph

import androidx.compose.animation.AnimatedContentTransitionScope
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
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_update_task.CreateUpdateTaskScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_update_task.CreateUpdateTaskViewModel
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.HomeScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding.OnboardingScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.SettingsScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.app_data.PlacesScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.task.TaskScreen
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.task.TaskViewModel
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
            route = "${Route.CreateUpdateTaskScreen.route}/{time_type_id}/{date_in_long}/{week_date_first_part_in_milliseconds}/{week_date_second_part_in_milliseconds}/{category_id}",
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = LinearEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = LinearEasing),
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
            }, navArgument("category_id") {
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
            val categoryId = it.arguments?.getString("category_id")!!.toInt()

            val viewModel: CreateUpdateTaskViewModel = hiltViewModel()
            viewModel.state.timeTypeId = timeTypeId
            viewModel.state.dateInMilliseconds = dateInMilliseconds
            viewModel.state.weekDate =
                Pair(Date(weekDateFirstPartInMilliseconds), Date(weekDateSecondPartInMilliseconds))
            viewModel.state.categoryId = categoryId

            CreateUpdateTaskScreen(
                navHostController = navHostController, viewModel = viewModel
            )
        }
        composable(route = "${Route.CreateUpdateTaskScreen.route}/{task_id}", enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            )
        }, exitTransition = {
            fadeOut(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            )
        }, arguments = listOf(navArgument("task_id") {
            NavType.StringType
            nullable = false
        })
        ) {
            val taskId = it.arguments?.getString("task_id")!!.toInt()

            val viewModel: CreateUpdateTaskViewModel = hiltViewModel()
            viewModel.state.taskId = taskId

            CreateUpdateTaskScreen(
                navHostController = navHostController, viewModel = viewModel
            )
        }
        composable(route = "${Route.TaskScreen.route}/{task_id}",
            arguments = listOf(navArgument("task_id") {
                NavType.StringType
                nullable = false
            }),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            }) {
            val taskId = it.arguments?.getString("task_id")!!.toInt()

            val viewModel: TaskViewModel = hiltViewModel()
            viewModel.state.taskId = taskId

            TaskScreen(navHostController, viewModel = viewModel)
        }
        composable(route = Route.SettingsScreen.route, enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            )
        }, exitTransition = {
            fadeOut(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            )
        }, popEnterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            )
        }, popExitTransition = {
            fadeOut(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            )
        }) {
            SettingsScreen(navHostController = navHostController, viewModel = hiltViewModel())
        }
        composable(route = Route.PlacesScreen.route, enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            )
        }, exitTransition = {
            fadeOut(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(
                    300, easing = LinearEasing
                )
            )
        }) {
            PlacesScreen(navHostController = navHostController, viewModel = hiltViewModel())
        }
    }
}