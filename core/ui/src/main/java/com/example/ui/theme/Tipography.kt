package com.example.ui.theme


import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.ui.R

val Coolvetica = FontFamily(
    Font(R.font.coolvetica_regular)
)

val SourceSerif = FontFamily(
    Font(R.font.source_serif_4_regular)
)

fun provideTypography(fontScale: Float): Typography {
    return Typography(
        bodyLarge = TextStyle(
            fontFamily = Coolvetica,
            fontSize = (36 * fontScale).sp
        ),
        bodyMedium = TextStyle(
            fontFamily = Coolvetica,
            fontSize = (21 * fontScale).sp
        ),
        titleLarge = TextStyle(
            fontFamily = Coolvetica,
            fontSize = (18 * fontScale).sp
        ),
        labelLarge = TextStyle(
            fontFamily = SourceSerif,
            fontSize = (18 * fontScale).sp
        ),
        labelMedium = TextStyle(
            fontFamily = SourceSerif,
            fontSize = (12 * fontScale).sp
        ),
        labelSmall = TextStyle(
            fontFamily = SourceSerif,
            fontSize = (10 * fontScale).sp
        )
    )
}