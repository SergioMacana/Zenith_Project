package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText

@Composable
fun NotificationsDrawerContent() {

    val colors = LocalZenithColors.current
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.notification),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = textColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        val notifications = listOf(
            "Notificacion 1",
            "Notificacion 2",
            "Notificacion 3"
        )

        notifications.forEachIndexed { index, text ->

            val isSelected = selectedIndex == index

            Column {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            selectedIndex = if (isSelected) null else index
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected)
                        MaterialTheme.colorScheme.surface
                    else
                        MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center,
                        color = colors.autoText(
                            if (isSelected)
                                MaterialTheme.colorScheme.surface
                            else
                                MaterialTheme.colorScheme.primary
                        )
                    )
                }

                if (isSelected) {
                    Text(
                        text = stringResource(R.string.check_notif),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                        color = textColor
                    )
                }
            }
        }
    }
}