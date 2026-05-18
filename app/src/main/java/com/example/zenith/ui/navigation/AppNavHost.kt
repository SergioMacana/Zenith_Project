package com.example.zenith.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.example.ui.screens.SplashScreen
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ui.screens.ConfigScreen
import com.example.ui.screens.FitnessScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoodScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.TrainingScreen
import com.example.ui.viewmodel.FitnessViewModel
import com.example.ui.viewmodel.SettingsViewModel
import com.example.ui.viewmodel.MoodViewModel
import com.example.ui.viewmodel.NotificationViewModel
import com.example.ui.viewmodel.TaskViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel,
    moodViewModel : MoodViewModel,
    notificationViewModel: NotificationViewModel,
    taskViewModel: TaskViewModel,
    fitnessViewModel: FitnessViewModel
){

    val settingsState by settingsViewModel.settingsState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route
    ) {

        composable(NavRoutes.Splash.route) {
            SplashScreen(
                onNavigateNext = {
                    val destination = if (settingsState.isFirstLaunch) {
                        NavRoutes.Config.route
                    } else {
                        NavRoutes.Home.route
                    }

                    navController.navigate(destination) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Config.route) {
            ConfigScreen(
                settingsViewModel = settingsViewModel,
                onNext = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Config.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Home.route) {
            HomeScreen(
                settingsViewModel = settingsViewModel,
                notificationViewModel = notificationViewModel,
                taskViewModel = taskViewModel,
                onGoToMood = {
                    navController.navigate(NavRoutes.Mood.route)
                },
                onGoToTasks = {
                    navController.navigate(NavRoutes.Tasks.route)
                },
                onGoToFitness = {
                    navController.navigate(NavRoutes.Fitness.route)
                }
            )
        }

        composable(NavRoutes.Mood.route) {
            MoodScreen(
                moodViewModel = moodViewModel,
                settingsViewModel = settingsViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.Tasks.route) {
            TasksScreen(
                taskViewModel = taskViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.Fitness.route) {
            FitnessScreen(
                onGoToTraining = { exerciseId ->
                    navController.navigate(NavRoutes.Training.createRoute(exerciseId))
                },
                fitnessViewModel = fitnessViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.Training.route,
            arguments = listOf(
                navArgument("exerciseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val exerciseId =
                backStackEntry.arguments?.getString("exerciseId") ?: return@composable

            TrainingScreen(
                exerciseId = exerciseId,
                fitnessViewModel = fitnessViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}