package com.example.zenith.ui.navigation

import androidx.compose.runtime.Composable
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

@Composable
fun AppNavHost(navController: NavHostController) {

    var selectedExercise by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route
    ) {

        composable(NavRoutes.Splash.route) {
            SplashScreen(
                onNavigateNext = {
                    navController.navigate(NavRoutes.Config.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Config.route) {
            ConfigScreen(
                onNext = {
                    navController.navigate(NavRoutes.Home.route)
                }
            )
        }

        composable(NavRoutes.Home.route) {
            HomeScreen(
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