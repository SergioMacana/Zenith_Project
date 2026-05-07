package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.TaskItem
import com.example.domain.model.TaskPresentation
import com.example.domain.model.toPresentation
import com.example.domain.usecase.task.CompleteTaskUseCase
import com.example.domain.usecase.task.CreateTaskUseCase
import com.example.domain.usecase.task.DeleteTaskUseCase
import com.example.domain.usecase.task.GetAllTasksUseCase
import com.example.domain.usecase.task.GetTaskByIdUseCase
import com.example.domain.usecase.task.GetTasksForDayUseCase
import com.example.domain.usecase.task.GetUpcomingTasksUseCase
import com.example.domain.usecase.task.UpdateTaskUseCase
import com.example.ui.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class TaskViewModel(
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getTasksForDayUseCase: GetTasksForDayUseCase,
    private val getUpcomingTasksUseCase: GetUpcomingTasksUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase
) : ViewModel()  {

    private val _pendingTasks = MutableStateFlow<List<TaskPresentation>>(emptyList())
    val pendingTasks: StateFlow<List<TaskPresentation>> = _pendingTasks.asStateFlow()

    private val _tasksForSelectedDay = MutableStateFlow<List<TaskPresentation>>(emptyList())
    val tasksForSelectedDay: StateFlow<List<TaskPresentation>> = _tasksForSelectedDay.asStateFlow()

    private val _tasksForSelectedMonth = MutableStateFlow<List<TaskPresentation>>(emptyList())
    val tasksForSelectedMonth: StateFlow<List<TaskPresentation>> = _tasksForSelectedMonth.asStateFlow()

    private val _selectedTask = MutableStateFlow<TaskPresentation?>(null)
    val selectedTask: StateFlow<TaskPresentation?> = _selectedTask.asStateFlow()

    private val _selectedDate = MutableStateFlow(DateUtils.todayStart())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    init {
        refreshPendingTasks()
    }

    fun selectDate(date: Long) {
        _selectedDate.value = date
        refreshTasksForSelectedDate()
        refreshTasksForSelectedMonth()
    }

    private fun refreshAll() {
        refreshPendingTasks()
        refreshTasksForSelectedDate()
        refreshTasksForSelectedMonth()
    }

    private fun refreshPendingTasks() {
        viewModelScope.launch {
            _pendingTasks.value = getAllTasksUseCase()
                .filter { !it.isCompleted }
                .map { it.toPresentation() }
        }
    }

    private fun refreshTasksForSelectedDate() {
        viewModelScope.launch {
            val start = _selectedDate.value
            val end = DateUtils.endOfDay(start)

            _tasksForSelectedDay.value = getTasksForDayUseCase(start, end)
                .map { it.toPresentation() }
        }
    }

    private fun refreshTasksForSelectedMonth() {
        viewModelScope.launch {

            val selectedMonth = Calendar.getInstance().apply {
                timeInMillis = _selectedDate.value
            }.get(Calendar.MONTH)

            val selectedYear = Calendar.getInstance().apply {
                timeInMillis = _selectedDate.value
            }.get(Calendar.YEAR)

            _tasksForSelectedMonth.value = getAllTasksUseCase()
                .filter { !it.isCompleted }
                .filter { task ->

                    val due = task.dueDate ?: return@filter false

                    val cal = Calendar.getInstance().apply {
                        timeInMillis = due
                    }

                    cal.get(Calendar.MONTH) == selectedMonth &&
                            cal.get(Calendar.YEAR) == selectedYear
                }
                .map { it.toPresentation() }
        }
    }

    fun selectTask(taskId: String) {
        viewModelScope.launch {
            _selectedTask.value = getTaskByIdUseCase(taskId)?.toPresentation()
        }
    }

    fun clearSelectedTask() {
        _selectedTask.value = null
    }

    fun createTask(task: TaskItem) {
        viewModelScope.launch {
            createTaskUseCase(task)
            refreshAll()
        }
    }

    fun updateTask(task: TaskItem) {
        viewModelScope.launch {
            updateTaskUseCase(task)
            refreshAll()
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            deleteTaskUseCase(taskId)
            refreshAll()
        }
    }

    fun completeTask(taskId: String) {
        viewModelScope.launch {
            completeTaskUseCase(taskId)
            refreshAll()
        }
    }
}