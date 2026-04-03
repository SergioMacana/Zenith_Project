package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.screens.SectionTitle

@Composable
fun TaskCarousel(onGoToTasks: () -> Unit) {

    Column {
        SectionTitle(text = stringResource(R.string.title_card_tasks))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {

            item {
                NewTaskCard(onGoToTasks = onGoToTasks)
            }

            items(
                listOf(
                    "Beber Agua" to "Mantente hidratado",
                    "Ir de compras" to "No olvidar la comida de Mike"
                )
            ) { (title, detail) ->

                TaskCard(
                    title = title,
                    detail = detail
                )
            }
        }
    }
}
