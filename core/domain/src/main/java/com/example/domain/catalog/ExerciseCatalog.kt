package com.example.domain.catalog

import com.example.domain.model.ExerciseInfo

object ExerciseCatalog {

    val exercises = listOf(
        ExerciseInfo("run", "Correr", "fit_correr"),
        ExerciseInfo("meditation", "Meditar", "fit_meditation"),
        ExerciseInfo("jump", "Saltar", "fit_saltar"),
        ExerciseInfo("squats", "Sentadillas", "fit_sentadillas"),
        ExerciseInfo("weights", "Pesas", "fit_pesas"),
        ExerciseInfo("abs", "Abdominales", "fit_abs"),
        ExerciseInfo("pushups", "Flexiones", "fit_flexing"),
        ExerciseInfo("stretch", "Estiramiento", "fit_estiramiento")
    )
}