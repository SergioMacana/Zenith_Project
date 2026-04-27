package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.domain.preferences.AppTheme
import com.example.domain.preferences.FontSizeOption
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText
import com.example.ui.theme.toThemeOption
import com.example.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsDrawerContent(
    settingsViewModel: SettingsViewModel
) {
    val settingsState by settingsViewModel.settingsState.collectAsState()

    val sizes = listOf("small", "medium", "big")
    var selectedSize by remember(settingsState.fontSize) {
        mutableStateOf(
            when (settingsState.fontSize) {
                FontSizeOption.SMALL -> "small"
                FontSizeOption.MEDIUM -> "medium"
                FontSizeOption.LARGE -> "big"
            }
        )
    }

    val themes = AppTheme.values().map { it.toThemeOption() }
    var selectedTheme by remember(settingsState.theme) {
        mutableStateOf(
            themes.first { it.appTheme == settingsState.theme }
        )
    }

    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.logo_app),
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.version),
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )

        Spacer(modifier = Modifier.height(24.dp))


        ExpandableItem(title = stringResource(R.string.button_text_size)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                sizes.forEach { size ->
                    SizeButton(
                        text = stringResource(
                            id = when (size) {
                                "small" -> R.string.small
                                "medium" -> R.string.medium
                                else -> R.string.big
                            }
                        ),
                        isSelected = selectedSize == size,
                        onClick = {
                            selectedSize = size

                            val fontOption = when (size) {
                                "small" -> FontSizeOption.SMALL
                                "medium" -> FontSizeOption.MEDIUM
                                else -> FontSizeOption.LARGE
                            }

                            settingsViewModel.updateFontSize(fontOption)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        ExpandableItem(title = stringResource(R.string.button_select_theme)) {
            ThemeSelector(
                themes = themes,
                selectedTheme = selectedTheme,
                onThemeSelected = {
                    selectedTheme = it
                    settingsViewModel.updateTheme(it.appTheme)
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
            )
        }

        ExpandableItem(title = stringResource(R.string.about)) {
            AboutSection()
        }
    }
}