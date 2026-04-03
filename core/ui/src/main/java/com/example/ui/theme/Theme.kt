package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(
    appTheme: AppTheme,
    fontSize: FontSizeOption,
    content: @Composable () -> Unit
) {
    val colors = paletteFor(appTheme)
    val typography = provideTypography(fontSize.scale)

    val materialColors = lightColorScheme(
        background = colors.primaryBg,
        surface = colors.secondaryBg,
        primary = colors.accent1,
        secondary = colors.accent2,
        onBackground = colors.textPrimary,
        onSurface = colors.textPrimary
    )

    CompositionLocalProvider(
        LocalZenithColors provides colors
    ) {
        MaterialTheme(
            colorScheme = materialColors,
            typography = typography,
            content = content
        )
    }
}