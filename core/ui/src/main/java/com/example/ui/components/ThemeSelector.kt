package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ThemeOption

@Composable
fun ThemeSelector(
    themes: List<ThemeOption>,
    selectedTheme: ThemeOption,
    onThemeSelected: (ThemeOption) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        themes.chunked(2).forEach { rowThemes ->

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                rowThemes.forEach { theme ->

                    ThemeItem(
                        theme = theme,
                        isSelected = theme == selectedTheme,
                        onClick = { onThemeSelected(theme) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}