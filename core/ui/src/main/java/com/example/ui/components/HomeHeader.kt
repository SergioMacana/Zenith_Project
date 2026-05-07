package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.example.ui.R
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText
import com.example.ui.viewmodel.SettingsViewModel

@Composable
fun HomeHeader(
    settingsViewModel: SettingsViewModel,
    onOpenLeft: () -> Unit,
    onOpenRight: () -> Unit
) {
    val settingsState by settingsViewModel.settingsState.collectAsState()

    val colors = LocalZenithColors.current
    val iconColor = colors.autoText(MaterialTheme.colorScheme.surface)

    AppHeader(
        title = "${stringResource(R.string.welcome_message)} ${settingsState.userName.ifBlank { "Usuario" }}",
        leftContent = {
            IconButton(onClick = onOpenLeft) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configuración",
                    tint = iconColor
                )
            }
        },

        rightContent = {
            IconButton(onClick = onOpenRight) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = iconColor
                )
            }
        }
    )
}