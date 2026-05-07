package com.example.ui.di

import android.content.Context
import com.example.data.local.fitness.FitnessLocalManager
import com.example.data.local.fitness.FitnessPrefsManager
import com.example.data.local.notification.NotificationLocalManager
import com.example.data.local.preferences.PrefsManager
import com.example.data.local.room.ZenithDatabase
import com.example.data.repository.FitnessRepositoryImpl
import com.example.data.repository.NotificationRepositoryImpl
import com.example.data.repository.TaskRepositoryImpl
import com.example.domain.repository.FitnessRepository
import com.example.domain.repository.NotificationRepository
import com.example.domain.repository.TaskRepository
import com.example.domain.usecase.fitness.CompleteExerciseUseCase
import com.example.domain.usecase.fitness.GetExerciseRoutineUseCase
import com.example.domain.usecase.fitness.GetExercisesUseCase
import com.example.domain.usecase.fitness.SaveExerciseRoutineUseCase
import com.example.domain.usecase.task.CompleteTaskUseCase
import com.example.domain.usecase.task.CreateTaskUseCase
import com.example.domain.usecase.task.DeleteTaskUseCase
import com.example.domain.usecase.task.GetAllTasksUseCase
import com.example.domain.usecase.task.GetTaskByIdUseCase
import com.example.domain.usecase.task.GetTasksForDayUseCase
import com.example.domain.usecase.task.GetUpcomingTasksUseCase
import com.example.domain.usecase.task.UpdateTaskUseCase
import com.example.ui.viewmodel.FitnessViewModel
import com.example.ui.viewmodel.TaskViewModel

class ZenithContainer(
    context: Context
) {

    private val appContext = context.applicationContext

    // -------------------------
    // COMMON MANAGERS
    // -------------------------

    private val notificationLocalManager =
        NotificationLocalManager.getInstance(appContext)

    private val prefsManager =
        PrefsManager(appContext)

    private val fitnessPrefsManager =
        FitnessPrefsManager(appContext)

    // -------------------------
    // ROOM DATABASE
    // -------------------------

    private val database = ZenithDatabase.getInstance(appContext)
    private val taskDao = database.taskDao()

    // -------------------------
    // TASK DATA
    // -------------------------

    private val taskRepository: TaskRepository =
        TaskRepositoryImpl(taskDao)

    // -------------------------
    // FITNESS DATA
    // -------------------------

    private val fitnessLocalManager = FitnessLocalManager(appContext)
    private val fitnessRepository: FitnessRepository =
        FitnessRepositoryImpl(fitnessLocalManager)

    // -------------------------
    // NOTIFICATION DATA
    // -------------------------

    private val notificationRepository: NotificationRepository =
        NotificationRepositoryImpl(notificationLocalManager)

    // ============================================================
    // TASK VIEWMODEL FACTORY
    // ============================================================

    val taskViewModelFactory = ZenithViewModelFactory {
        TaskViewModel(
            createTaskUseCase = CreateTaskUseCase(taskRepository),
            updateTaskUseCase = UpdateTaskUseCase(taskRepository),
            deleteTaskUseCase = DeleteTaskUseCase(taskRepository),
            completeTaskUseCase = CompleteTaskUseCase(taskRepository),
            getAllTasksUseCase = GetAllTasksUseCase(taskRepository),
            getTasksForDayUseCase = GetTasksForDayUseCase(taskRepository),
            getUpcomingTasksUseCase = GetUpcomingTasksUseCase(taskRepository),
            getTaskByIdUseCase = GetTaskByIdUseCase(taskRepository)
        )
    }

    // ============================================================
    // FITNESS VIEWMODEL FACTORY
    // ============================================================

    val fitnessViewModelFactory = ZenithViewModelFactory {
        FitnessViewModel(
            getExercisesUseCase = GetExercisesUseCase(fitnessRepository),
            getExerciseRoutineUseCase = GetExerciseRoutineUseCase(fitnessRepository),
            saveExerciseRoutineUseCase = SaveExerciseRoutineUseCase(fitnessRepository),
            completeExerciseUseCase = CompleteExerciseUseCase(fitnessRepository),
            fitnessPrefsManager = fitnessPrefsManager,
            notificationLocalManager = notificationLocalManager
        )
    }
}