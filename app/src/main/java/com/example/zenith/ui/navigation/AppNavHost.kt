package com.example.zenith.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.example.ui.screens.SplashScreen
import androidx.navigation.compose.*
import com.example.ui.screens.ConfigScreen
import com.example.ui.screens.FitnessScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoodScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.TrainingScreen
import com.example.ui.viewmodel.SettingsViewModel
import com.example.ui.viewmodel.MoodViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel,
    moodViewModel : MoodViewModel
){

    val settingsState by settingsViewModel.settingsState.collectAsState()

    var selectedExercise by remember { mutableStateOf("") }

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
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.Fitness.route) {
            FitnessScreen(
                onGoToTraining = { exercise ->
                    selectedExercise = exercise
                    navController.navigate(NavRoutes.Training.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Training.route) {
            TrainingScreen(
                exerciseName = selectedExercise,
                onBack = { navController.popBackStack() }
            )
        }
    }
}