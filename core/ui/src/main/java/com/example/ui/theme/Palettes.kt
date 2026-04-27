package com.example.ui.theme

import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.example.domain.preferences.AppTheme
import com.example.ui.R

@Composable
fun paletteFor(theme: AppTheme): ZenithColors {

    @Composable
    fun c(@ColorRes id: Int) = colorResource(id)

    return when (theme) {
        AppTheme.CLASSIC -> ZenithColors(
            primaryBg = c(R.color.classic_primary_bg),
            secondaryBg = c(R.color.classic_secondary_bg),

            accent1 = c(R.color.classic_accent_1),
            accent2 = c(R.color.classic_accent_2),
            accent3 = c(R.color.classic_accent_3),
            accent4 = c(R.color.classic_accent_4),

            textPrimary = c(R.color.classic_text_primary),
            textSecondary = c(R.color.classic_text_secondary),

            highlight = c(R.color.classic_preview_accent)
        )
        AppTheme.CLASSIC_DARK -> ZenithColors(
            primaryBg = c(R.color.classic_dark_primary_bg),
            secondaryBg = c(R.color.classic_dark_secondary_bg),

            accent1 = c(R.color.classic_dark_accent_1),
            accent2 = c(R.color.classic_dark_accent_2),
            accent3 = c(R.color.classic_dark_accent_3),
            accent4 = c(R.color.classic_dark_accent_4),

            textPrimary = c(R.color.classic_dark_text_primary),
            textSecondary = c(R.color.classic_dark_text_secondary),

            highlight = c(R.color.classic_dark_preview_accent)
        )
        AppTheme.LOW_PINK -> ZenithColors(
            primaryBg = c(R.color.lowpink_primary_bg),
            secondaryBg = c(R.color.lowpink_secondary_bg),

            accent1 = c(R.color.lowpink_accent_1),
            accent2 = c(R.color.lowpink_accent_2),
            accent3 = c(R.color.lowpink_accent_3),
            accent4 = c(R.color.lowpink_accent_4),

            textPrimary = c(R.color.lowpink_text_primary),
            textSecondary = c(R.color.lowpink_text_secondary),

            highlight = c(R.color.lowpink_preview_accent)
        )
        AppTheme.GOLD_ESMERALD -> ZenithColors(
            primaryBg = c(R.color.gold_primary_bg),
            secondaryBg = c(R.color.gold_secondary_bg),

            accent1 = c(R.color.gold_accent_1),
            accent2 = c(R.color.gold_accent_2),
            accent3 = c(R.color.gold_accent_3),
            accent4 = c(R.color.gold_accent_4),

            textPrimary = c(R.color.gold_text_primary),
            textSecondary = c(R.color.gold_text_secondary),

            highlight = c(R.color.gold_preview_accent)
        )
    }
}
@Composable
fun AppTheme.toThemeOption(): ThemeOption {

    val palette = paletteFor(this)

    return ThemeOption(
        appTheme = this,
        name = this.name,
        accentColors = listOf(
            palette.accent1,
            palette.accent2,
            palette.accent3,
            palette.accent4
        ),
        background = palette.secondaryBg,
        highlight = palette.highlight
    )
}