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
import com.example.ui.R
import com.example.ui.screens.SectionTitle
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText

@Composable
fun TodayCard() {

    val colors = LocalZenithColors.current
    val textColor = colors.autoText(colors.accent1)

    Column {
        SectionTitle(text = stringResource(R.string.today))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(16.dp),
            color = colors.accent1
        ) {
            Row(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Lunes",
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor
                    )

                    Text(
                        text = "28",
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )

                    Text(
                        text = "Febrero",
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    reverseLayout = false
                ) {

                    items(5) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Registrar emoción",
                                style = MaterialTheme.typography.titleLarge,
                                color = textColor
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "08:00 am",
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