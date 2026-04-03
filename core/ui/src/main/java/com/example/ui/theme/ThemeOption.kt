package com.example.ui.theme

import androidx.compose.ui.graphics.Color


data class ThemeOption(
    val appTheme: AppTheme,
    val name: String,
    val accentColors: List<Color>,
    val background: Color,
    val highlight: Color
)
