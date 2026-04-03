package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.screens.SectionTitle
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText

@Composable
fun MoodyCard(
    onClick: () -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(colors.accent2)

    Column {
        SectionTitle(text = stringResource(R.string.title_card_moody))

        SplitCard(
            onClick = onClick,
            leftContent = {
                Text(
                    text = stringResource(R.string.card_moody),
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
            },
            rightContent = {
                Image(
                    painter = painterResource(R.drawable.emoji_pensativo),
                    contentDescription = "Mood",
                    modifier = Modifier.size(140.dp)
                )
            },
            colorCard = colors.accent2
        )
    }
}