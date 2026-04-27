package com.example.zenith.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.domain.preferences.AppTheme
import com.example.domain.preferences.FontSizeOption
import com.example.ui.theme.AppTheme

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ZenithTheme(
    selectedTheme: AppTheme,
    selectedFontSize: FontSizeOption,
    content: @Composable () -> Unit
) {
    AppTheme(
        appTheme = selectedTheme,
        fontSize = selectedFontSize
    ) {
        content()
    }
}