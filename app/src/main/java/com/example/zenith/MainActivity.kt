package com.example.zenith

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ui.di.FitnessViewModelFactory
import com.example.ui.di.MoodViewModelFactory
import com.example.ui.di.NotificationViewModelFactory
import com.example.ui.di.SettingsViewModelFactory
import com.example.ui.di.ZenithContainer
import com.example.ui.receivers.TaskReminderScheduler
import com.example.ui.viewmodel.FitnessViewModel
import com.example.ui.viewmodel.MoodViewModel
import com.example.ui.viewmodel.NotificationViewModel
import com.example.ui.viewmodel.SettingsViewModel
import com.example.ui.viewmodel.TaskViewModel
import com.example.zenith.receivers.FitnessReminderScheduler
import com.example.zenith.ui.navigation.AppNavHost
import com.example.zenith.ui.theme.ZenithTheme

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                scheduleAllReminderSystems()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermissionIfNeeded()
        CleanupScheduler.scheduleDailyCleanup(this)

        setContent {

            val zenithContainer = remember { ZenithContainer(this) }

            val taskViewModel: TaskViewModel = viewModel(
                factory = zenithContainer.taskViewModelFactory
            )

            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(applicationContext)
            )

            val moodViewModel: MoodViewModel = viewModel(
                factory = MoodViewModelFactory(applicationContext)
            )

            val notificationViewModel: NotificationViewModel = viewModel(
                factory = NotificationViewModelFactory(applicationContext)
            )

            val fitnessViewModel: FitnessViewModel = viewModel(
                factory = FitnessViewModelFactory(applicationContext)
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
                    moodViewModel = moodViewModel,
                    notificationViewModel = notificationViewModel,
                    taskViewModel = taskViewModel,
                    fitnessViewModel = fitnessViewModel
                )
            }
        }
    }

    private fun scheduleAllReminderSystems() {
        ReminderScheduler.scheduleDailyMoodReminder(this)
        FitnessReminderScheduler.scheduleMorningReminder(this)
        FitnessReminderScheduler.scheduleHabitReminder(this)
        TaskReminderScheduler.scheduleDailySummary(this)
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                scheduleAllReminderSystems()
            } else {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

        } else {
            scheduleAllReminderSystems()
        }
    }
}