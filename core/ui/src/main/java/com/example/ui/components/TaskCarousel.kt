package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.domain.model.TaskItem
import com.example.ui.R
import com.example.ui.screens.SectionTitle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskCarousel(
    tasks: List<TaskItem>,
    onGoToTasks: () -> Unit
) {
    Column {
        SectionTitle(text = stringResource(R.string.title_card_tasks))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {

            item {
                NewTaskCard(onGoToTasks = onGoToTasks)
            }

            items(tasks) { task ->

                val formattedTime =
                    task.dueDate?.let { dueDate ->
                        SimpleDateFormat(
                            "hh:mm a",
                            Locale.getDefault()
                        ).format(Date(dueDate))
                    } ?: "--:--"

                TaskCard(
                    title = task.title,
                    detail = task.description,
                    time = formattedTime,
                    onClick = onGoToTasks
                )
            }
        }
    }
}
