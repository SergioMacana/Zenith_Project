package com.example.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

data class ZenithColors(
    val primaryBg: Color,
    val secondaryBg: Color,

    val accent1: Color,
    val accent2: Color,
    val accent3: Color,
    val accent4: Color,

    val textPrimary: Color,
    val textSecondary: Color,

    val highlight: Color
)

val Color.isLight: Boolean
    get() = luminance() <  0.5f

val LocalZenithColors = staticCompositionLocalOf<ZenithColors> {
    error("No ZenithColors provided")
}

fun ZenithColors.autoText(background: Color): Color {
    return if (background.isLight) textPrimary else textSecondary
}