package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText

@Composable
fun NewTaskCard(onGoToTasks: () -> Unit) {

    val colors = LocalZenithColors.current
    val textColor = colors.autoText(colors.accent3)

    Surface(
        modifier = Modifier
            .width(140.dp)
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        color = colors.accent3
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = stringResource(R.string.new_task),
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor
                )

                Text(
                    text = stringResource(R.string.task_details),
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor
                )
            }

            Button(
                onClick = onGoToTasks,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.textPrimary,
                    contentColor = colors.accent3
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.go),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}