package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.ui.components.FitnessCard
import com.example.ui.components.SettingsDrawerContent
import com.example.ui.components.HomeHeader
import com.example.ui.components.LeftDrawer
import com.example.ui.components.MoodyCard
import com.example.ui.components.NotificationsDrawerContent
import com.example.ui.components.RightDrawer
import com.example.ui.components.TaskCarousel
import com.example.ui.components.TodayCard
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText
import com.example.ui.viewmodel.NotificationViewModel
import com.example.ui.viewmodel.SettingsViewModel
import com.example.ui.viewmodel.TaskViewModel

@Composable
fun HomeScreen(
    settingsViewModel: SettingsViewModel,
    notificationViewModel: NotificationViewModel,
    taskViewModel: TaskViewModel,
    onGoToMood: () -> Unit,
    onGoToTasks: () -> Unit,
    onGoToFitness: () -> Unit
) {
    var isLeftPanelOpen by remember { mutableStateOf(false) }
    var isRightPanelOpen by remember { mutableStateOf(false) }

    val notificationsState by notificationViewModel.notificationsState.collectAsState()
    val upcomingTasks by taskViewModel.upcomingTasks.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->

                    val threshold = 50

                    if (change.position.x < 50 && dragAmount > threshold) {
                        isLeftPanelOpen = true
                        isRightPanelOpen = false
                    }

                    if (change.position.x > size.width - 50 && dragAmount < -threshold) {
                        isRightPanelOpen = true
                        isLeftPanelOpen = false
                    }

                    if (isLeftPanelOpen && dragAmount < -threshold) {
                        isLeftPanelOpen = false
                    }

                    if (isRightPanelOpen && dragAmount > threshold) {
                        isRightPanelOpen = false
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeHeader(
                settingsViewModel = settingsViewModel,
                onOpenLeft = {
                    isLeftPanelOpen = true
                    isRightPanelOpen = false
                },
                onOpenRight = {
                    isRightPanelOpen = true
                    isLeftPanelOpen = false
                }
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item { TodayCard(
                    notifications = notificationsState
                )}

                item { MoodyCard(onClick = onGoToMood) }

                item {
                    TaskCarousel(
                        tasks = upcomingTasks,
                        onGoToTasks = onGoToTasks
                    )
                }

                item { FitnessCard(onClick = onGoToFitness) }
            }

        }
        if (isLeftPanelOpen || isRightPanelOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable {
                        isLeftPanelOpen = false
                        isRightPanelOpen = false
                    }
            )
        }

        LeftDrawer(
            isOpen = isLeftPanelOpen,
            onClose = { isLeftPanelOpen = false }
        ) {
            SettingsDrawerContent(
                settingsViewModel = settingsViewModel
            )
        }

        RightDrawer(
            isOpen = isRightPanelOpen,
            onClose = { isRightPanelOpen = false }
        ) {
            NotificationsDrawerContent(
                notifications = notificationsState,
                onMarkAsRead = { id ->
                    notificationViewModel.markAsRead(id)
                }
            )
        }
    }
}

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = textColor,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 8.dp)
    )
}