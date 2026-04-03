package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.components.AppFab
import com.example.ui.components.AppHeader
import com.example.ui.components.SizeButton
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText

@Composable
fun TrainingScreen(
    exerciseName: String,
    onBack: () -> Unit,
    onEdit: () -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            AppHeader(
                title = exerciseName
            )

            TrainingContent()
        }

        TrainingFABs(
            onBack = onBack,
            onEdit = { showDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
    if (showDialog) {
        TrainingEditDialog(
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun TrainingContent() {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.subtitle_training),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 12.dp, bottom = 16.dp),
            color = textColor
        )

        TrainingStatsRow()

        TrainingTimerSection(
            exerciseImage = R.drawable.fit_correr
        )

        Spacer(modifier = Modifier.height(16.dp))

        TrainingProgressSection()
    }

}
@Composable
fun TrainingStatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TrainingStatItem("03", stringResource(R.string.series))
        TrainingStatItem("12", stringResource(R.string.reps))
        TrainingStatItem("60s", stringResource(R.string.workout))
        TrainingStatItem("120s", stringResource(R.string.rest))
    }
}
@Composable
fun TrainingStatItem(
    value: String,
    label: String
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Box(
        modifier = Modifier
            .size(width = 70.dp, height = 70.dp)
            .border(
                width = 2.dp,
                color = colors.highlight,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = textColor
            )
        }
    }
}
@Composable
fun TrainingTimerSection(
    exerciseImage: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TimerTopRow(exerciseImage)

        Spacer(modifier = Modifier.height(24.dp))

        TimerDisplay()

        Spacer(modifier = Modifier.height(16.dp))


    }
}
@Composable
fun TimerTopRow(
    exerciseImage: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = exerciseImage),
            contentDescription = "Exercise",
            modifier = Modifier.size(150.dp)
        )

        CircularTimer()
    }
}

@Composable
fun CircularTimer() {

    val colors = LocalZenithColors.current

    var isRunning by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0.3f) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(150.dp)
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            drawCircle(
                color = colors.secondaryBg,
                style = Stroke(width = 8.dp.toPx())
            )

            drawArc(
                color = colors.highlight,
                startAngle = -90f,
                sweepAngle = progress * 360f,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        IconButton(
            onClick = {
                isRunning = !isRunning
                progress = if (isRunning) 0.7f else 0.3f
            }
        ) {
            Icon(
                imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play/Pause",
                tint = colors.highlight
            )
        }
    }
}

@Composable
fun TimerDisplay() {

    val time = "00:01:20"

    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Box(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .width(200.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )

            TimerControls()
        }
    }
}

@Composable
fun TimerControls() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SizeButton(
            text = stringResource(R.string.button_stop),
            isSelected = true,
            onClick = {},
            modifier = Modifier.width(90.dp)
        )

        SizeButton(
            text = stringResource(R.string.button_restart),
            isSelected = true,
            onClick = {},
            modifier = Modifier.width(90.dp)
        )
    }
}
@Composable
fun TrainingProgressSection() {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {

        Text(
            text = stringResource(R.string.progress),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 12.dp, bottom = 16.dp),
            color = textColor
        )

        ProgressItem(
            title = "Serie 01 de 12",
            progress = 0.1f
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProgressItem(
            title = "Repeticiones 01 de 12",
            progress = 0.1f
        )
    }
}

@Composable
fun ProgressItem(
    title: String,
    progress: Float
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 8.dp),
            color = textColor
        )

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = colors.highlight,
            trackColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun TrainingEditDialog(
    onDismiss: () -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    var series by remember { mutableStateOf(3f) }
    var reps by remember { mutableStateOf(12f) }
    var workoutTime by remember { mutableStateOf(60f) }
    var restTime by remember { mutableStateOf(120f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        containerColor = MaterialTheme.colorScheme.background,
        title = {
            Text(stringResource(R.string.dialog_training), color =textColor, style = MaterialTheme.typography.bodyMedium,)
        },
        text = {

            Column {

                SliderItem(
                    title = stringResource(R.string.dialog_series),
                    value = series,
                    range = 1f..12f,
                    onValueChange = { series = it }
                )

                SliderItem(
                    title = stringResource(R.string.dialog_reps),
                    value = reps,
                    range = 1f..30f,
                    onValueChange = { reps = it }
                )

                SliderItem(
                    title = stringResource(R.string.dialog_time_training),
                    value = workoutTime,
                    range = 1f..300f,
                    suffix = "s",
                    onValueChange = { workoutTime = it }
                )

                SliderItem(
                    title = stringResource(R.string.dialog_time_rest),
                    value = restTime,
                    range = 1f..300f,
                    suffix = "s",
                    onValueChange = { restTime = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    SizeButton(
                        text = stringResource(R.string.button_cancel),
                        isSelected = false,
                        onClick = onDismiss
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    SizeButton(
                        text = stringResource(R.string.button_save),
                        isSelected = true,
                        onClick = {
                            onDismiss()
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun SliderItem(
    title: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    suffix: String = ""
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Column(
        modifier = Modifier.padding(bottom = 12.dp)
    ) {

        Text(
            text = "$title: ${value.toInt()}$suffix",
            style = MaterialTheme.typography.labelMedium,
            color = textColor
        )

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = colors.highlight,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.surface,
            ),
        )
    }
}

@Composable
fun TrainingFABs(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(colors.accent2)

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SmallFloatingActionButton(
            onClick = onEdit,
            containerColor = MaterialTheme.colorScheme.secondary,
            shape = CircleShape,
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = textColor
            )
        }

        AppFab(
            icon = Icons.Default.ArrowBack,
            onClick = onBack
        )
    }
}