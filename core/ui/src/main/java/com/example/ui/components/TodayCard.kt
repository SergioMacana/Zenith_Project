package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.domain.model.NotificationItem
import com.example.ui.R
import com.example.ui.screens.SectionTitle
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText

@Composable
fun TodayCard(
    notifications: List<NotificationItem>
){

    val colors = LocalZenithColors.current
    val textColor = colors.autoText(colors.accent1)

    val calendar = java.util.Calendar.getInstance()

    val dayName = java.text.SimpleDateFormat("EEEE", java.util.Locale("es")).format(calendar.time)
        .replaceFirstChar { it.uppercase() }

    val dayNumber = java.text.SimpleDateFormat("dd", java.util.Locale("es")).format(calendar.time)

    val monthName = java.text.SimpleDateFormat("MMMM", java.util.Locale("es")).format(calendar.time)
        .replaceFirstChar { it.uppercase() }

    val todayItems = notifications.take(5)

    val timeFormatter = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())

    Column {
        SectionTitle(text = stringResource(R.string.today))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(16.dp),
            color = colors.accent1
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor
                    )

                    Text(
                        text = dayNumber,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )

                    Text(
                        text = monthName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                }

                if (todayItems.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = stringResource(R.string.no_today_items),
                            style = MaterialTheme.typography.labelMedium,
                            color = textColor
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(todayItems.size) { index ->
                            val item = todayItems[index]

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = textColor
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = timeFormatter.format(java.util.Date(item.timestamp)),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = textColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}