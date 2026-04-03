package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText

@Composable
fun ExpandableItem(
    title: String,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.surface)

    Column(modifier = Modifier.fillMaxWidth()) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable { expanded = !expanded },
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                color = textColor
            )
        }

        AnimatedVisibility(visible = expanded) {
            Box(modifier = Modifier.padding(8.dp)) {
                content()
            }
        }
    }
}