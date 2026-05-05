package com.example.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.components.AppFab
import com.example.ui.components.AppHeader
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText
import com.example.ui.viewmodel.FitnessViewModel

@Composable
fun FitnessScreen(
    fitnessViewModel: FitnessViewModel,
    onGoToTraining: (String) -> Unit,
    onBack: () -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    val exerciseDomainList by fitnessViewModel.exercises.collectAsState()

    val exercises = remember(exerciseDomainList) {
        exerciseDomainList.map { domainExercise ->
            ExerciseUi(
                name = domainExercise.title,
                iconRes = exerciseDrawableFor(domainExercise.id),
                id = domainExercise.id
            )
        }
    }
    Scaffold(
        floatingActionButton = {
            AppFab(
                icon = Icons.Default.ArrowBack,
                onClick = onBack,
                contentDescription = "Volver"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AppHeader(
                title = stringResource(id = R.string.title_fitness)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.subtitle_fitness),
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(exercises) { exercise ->
                        ExerciseItem(
                            exercise = exercise,
                            onClick = {
                                onGoToTraining(exercise.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: ExerciseUi,
    onClick: () -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Box (modifier = Modifier
        .fillMaxSize()
        .size(width = 170.dp, height = 170.dp)
        .background(MaterialTheme.colorScheme.surface)
        .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
        ) {
            Image(
                painter = painterResource(id = exercise.iconRes),
                contentDescription = exercise.name,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exercise.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}

data class ExerciseUi(
    val id: String,
    val name: String,
    @DrawableRes val iconRes: Int
)

fun exerciseDrawableFor(id: String): Int {
    return when (id) {
        "run" -> R.drawable.fit_correr
        "meditation" -> R.drawable.fit_meditation
        "jump" -> R.drawable.fit_saltar
        "squats" -> R.drawable.fit_sentadillas
        "weights" -> R.drawable.fit_pesas
        "abs" -> R.drawable.fit_abs
        "pushups" -> R.drawable.fit_flexing
        "stretch" -> R.drawable.fit_estiramiento
        else -> R.drawable.fit_correr
    }
}