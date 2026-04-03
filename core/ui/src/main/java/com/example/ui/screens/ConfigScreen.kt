package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.components.AppHeader
import com.example.ui.components.AppTextField
import com.example.ui.components.SizeButton
import com.example.ui.components.ThemeSelector
import com.example.ui.theme.AppTheme
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText
import com.example.ui.theme.toThemeOption

@Composable
fun ConfigScreen(
    onNext: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedSize by remember { mutableStateOf("medium") }

    val sizes = listOf("small", "medium", "big")

    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    val themes = AppTheme.values().map { it.toThemeOption() }

    var selectedTheme by remember { mutableStateOf(themes.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        AppHeader(
            title = stringResource(id = R.string.config_inicial)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(id = R.string.name_ask),
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = name,
                onValueChange = { name = it },
                hint = stringResource(id = R.string.name_input),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.size_text),
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                        onClick = { selectedSize = size }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.theme_app),
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            ThemeSelector(
                themes = themes,
                selectedTheme = selectedTheme,
                onThemeSelected = { selectedTheme = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            SizeButton(
                text = stringResource(id = R.string.save_continue),
                isSelected = true,
                onClick = onNext,
                modifier = Modifier.width(180.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

}