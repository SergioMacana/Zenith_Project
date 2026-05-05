package com.example.zenith.ui.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Config : NavRoutes("config")
    object Home : NavRoutes("home")
    object Mood : NavRoutes("mood")
    object Tasks : NavRoutes("tasks")
    object Fitness : NavRoutes("fitness")
    object Training : NavRoutes("training/{exerciseId}") {
        fun createRoute(exerciseId: String) = "training/$exerciseId"
    }
}