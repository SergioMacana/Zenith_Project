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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.domain.model.NotificationItem
import com.example.ui.R
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText

@Composable
fun NotificationsDrawerContent(
    notifications: List<NotificationItem>,
    onMarkAsRead: (String) -> Unit
){

    val colors = LocalZenithColors.current
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

        notifications.forEach { notification ->

            val isRead = notification.isRead

            Column {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            if (!isRead) {
                                onMarkAsRead(notification.id)
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isRead)
                        MaterialTheme.colorScheme.surface
                    else
                        MaterialTheme.colorScheme.primary
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = notification.title,
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.autoText(
                                if (isRead)
                                    MaterialTheme.colorScheme.surface
                                else
                                    MaterialTheme.colorScheme.primary
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = notification.message,
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.autoText(
                                if (isRead)
                                    MaterialTheme.colorScheme.surface
                                else
                                    MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                if (isRead) {
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