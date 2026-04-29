package com.example.zenith

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ui.di.MoodViewModelFactory
import com.example.ui.di.SettingsViewModelFactory
import com.example.ui.viewmodel.MoodViewModel
import com.example.ui.viewmodel.SettingsViewModel
import com.example.zenith.ui.navigation.AppNavHost
import com.example.zenith.ui.theme.ZenithTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(applicationContext)
            )

            val moodViewModel: MoodViewModel = viewModel(
                factory = MoodViewModelFactory(applicationContext)
            )

            val settingsState by settingsViewModel.settingsState.collectAsState()

            val navController = rememberNavController()

            ZenithTheme(
                selectedTheme = settingsState.theme,
                selectedFontSize = settingsState.fontSize
            ) {
                AppNavHost(
                    navController = navController,
                    settingsViewModel = settingsViewModel,
                    moodViewModel = moodViewModel
                )
            }
        }
    }
}