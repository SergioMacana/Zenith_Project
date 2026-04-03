package com.example.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText

@Composable
fun AppHeader(
    title: String,
    modifier: Modifier = Modifier,
    leftContent: (@Composable () -> Unit)? = null,
    rightContent: (@Composable () -> Unit)? = null
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .statusBarsPadding(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // LEFT
            if (leftContent != null) {
                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    leftContent()
                }
            }

            // TITLE
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )

            // RIGHT
            if (rightContent != null) {
                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    rightContent()
                }
            }
        }
    }
}