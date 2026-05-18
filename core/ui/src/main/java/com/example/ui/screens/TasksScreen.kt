package com.example.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.domain.model.TaskItem
import com.example.domain.model.TaskPresentation
import com.example.ui.R
import com.example.ui.components.AppFab
import com.example.ui.components.AppTextField
import com.example.ui.components.BaseSwitchScreen
import com.example.ui.components.SizeButton
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText
import com.example.ui.utils.DateUtils
import com.example.ui.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksScreen(
    taskViewModel: TaskViewModel,
    onBack: () -> Unit
){
    val pendingTasks by taskViewModel.pendingTasks.collectAsState()
    val tasksForSelectedDay by taskViewModel.tasksForSelectedDay.collectAsState()
    val tasksForSelectedMonth by taskViewModel.tasksForSelectedMonth.collectAsState()
    val selectedDate by taskViewModel.selectedDate.collectAsState()

    var selectedIndex by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMode by remember { mutableStateOf<TaskDialogMode>(TaskDialogMode.CREATE) }
    var currentTask by remember { mutableStateOf<TaskPresentation?>(null) }

    val titles = listOf(
        stringResource(R.string.title_tasks_1),
        stringResource(R.string.title_tasks_2)
    )

    val options = listOf(stringResource(R.string.opc_week),
        stringResource(R.string.opc_month))

    BaseSwitchScreen(
        title = titles[selectedIndex],
        options = options,
        selectedIndex = selectedIndex,
        onOptionSelected = { selectedIndex = it },
        onFabClick = onBack,
        onExtraFabClick = {
            dialogMode = TaskDialogMode.CREATE
            currentTask = null
            showDialog = true
        },
        extraFab = { onClick ->
            AppFab(
                icon = Icons.Default.Add,
                onClick = onClick,
                modifier = Modifier.size(56.dp),
                contentDescription = "Crear tarea"
            )
        }
    ) {
        TaskContentPager(
            selectedIndex = selectedIndex,
            onPageChanged = { selectedIndex = it },
            selectedDate = selectedDate,
            pendingTasks = pendingTasks,
            weeklyTasks = tasksForSelectedDay,
            monthlyTasks = tasksForSelectedMonth,
            onDateSelected = { taskViewModel.selectDate(it) },
            onEdit = { task ->
                dialogMode = TaskDialogMode.EDIT
                currentTask = task
                showDialog = true
            },
            onComplete = { task ->
                dialogMode = TaskDialogMode.COMPLETE
                currentTask = task
                showDialog = true
            }
        )
    }
    if (showDialog) {
        TaskDialog(
            mode = dialogMode,
            task = currentTask,
            onDismiss = { showDialog = false },

            onSave = { title, description, date, time ->

                if (dialogMode == TaskDialogMode.CREATE) {
                    taskViewModel.createTask(
                        TaskItem(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            description = description,
                            dueDate = date,
                            dueTimeLabel = time,
                            isCompleted = false,
                            reminderEnabled = true,
                            createdAt = System.currentTimeMillis()
                        )
                    )
                } else {
                    taskViewModel.updateTask(
                        currentTask!!.toDomain().copy(
                            title = title,
                            description = description,
                            dueDate = date,
                            dueTimeLabel = time
                        )
                    )
                }

                showDialog = false
            },

            onDelete = {
                currentTask?.let {
                    taskViewModel.deleteTask(it.id)
                }
                showDialog = false
            },

            onConfirmComplete = {
                currentTask?.let {
                    taskViewModel.completeTask(it.id)
                }
                showDialog = false
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskContentPager(
    selectedIndex: Int,
    onPageChanged: (Int) -> Unit,
    selectedDate: Long,
    pendingTasks: List<TaskPresentation>,
    weeklyTasks: List<TaskPresentation>,
    monthlyTasks: List<TaskPresentation>,
    onDateSelected: (Long) -> Unit,
    onEdit: (TaskPresentation) -> Unit,
    onComplete: (TaskPresentation) -> Unit
){
    val pagerState = rememberPagerState(
        initialPage = selectedIndex,
        pageCount = { 2 }
    )

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != selectedIndex) {
            onPageChanged(pagerState.currentPage)
        }
    }

    LaunchedEffect(selectedIndex) {
        if (pagerState.currentPage != selectedIndex) {
            pagerState.animateScrollToPage(selectedIndex)
        }
    }

    HorizontalPager(state = pagerState) { page ->

        when (page) {
            0 -> WeeklyTasksScreen(
                selectedDate = selectedDate,
                pendingTasks = pendingTasks,
                tasks = weeklyTasks,
                onDateSelected = onDateSelected,
                onEdit = onEdit,
                onComplete = onComplete
            )

            1 -> MonthlyTasksScreen(
                selectedDate = selectedDate,
                pendingTasks = pendingTasks,
                tasks = monthlyTasks,
                onDateSelected = onDateSelected,
                onEdit = onEdit,
                onComplete = onComplete
            )
        }
    }
}

@Composable
fun WeeklyTasksScreen(
    selectedDate: Long,
    pendingTasks: List<TaskPresentation>,
    tasks: List<TaskPresentation>,
    onDateSelected: (Long) -> Unit,
    onEdit: (TaskPresentation) -> Unit,
    onComplete: (TaskPresentation) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        WeeklyCalendar(
            selectedDate = selectedDate,
            pendingTasks = pendingTasks,
            onDateSelected = onDateSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskListSection(
            tasks = tasks,
            isMonthly = false,
            onEdit = onEdit,
            onComplete = onComplete
        )
    }
}

@Composable
fun MonthlyTasksScreen(
    selectedDate: Long,
    pendingTasks: List<TaskPresentation>,
    tasks: List<TaskPresentation>,
    onDateSelected: (Long) -> Unit,
    onEdit: (TaskPresentation) -> Unit,
    onComplete: (TaskPresentation) -> Unit
) {
    Column {
        MonthlyCalendar(
            selectedDate = selectedDate,
            pendingTasks = pendingTasks,
            onDateSelected = onDateSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskListSection(
            tasks = tasks,
            isMonthly = true,
            onEdit = onEdit,
            onComplete = onComplete
        )
    }
}

@Composable
fun WeeklyCalendar(
    selectedDate: Long,
    pendingTasks: List<TaskPresentation>,
    onDateSelected: (Long) -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    val today = DateUtils.todayStart()
    val startOfWeek = DateUtils.startOfWeek()
    val daysOfWeek = DateUtils.weekDays()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(daysOfWeek) { date ->

            val isToday = date == today
            val isSelected = date == selectedDate

            val hasTasks = pendingTasks.any { task ->
                val taskDay = DateUtils.todayStart() + (
                        (task.dueDate - DateUtils.todayStart()) /
                                (24 * 60 * 60 * 1000)
                        ) * (24 * 60 * 60 * 1000)

                taskDay == date
            }

            val backgroundColor = if (isSelected) {
                colors.highlight
            } else {
                colors.secondaryBg
            }

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(backgroundColor)
                    .clickable { onDateSelected(date) },
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = DateUtils.formatDayNumber(date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )

                    Text(
                        text = DateUtils.formatWeekName(date),
                        style = MaterialTheme.typography.labelMedium,
                        color = textColor
                    )
                }

                // Hoy
                if (isToday) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(4.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                    )
                }

                // Tiene tareas
                if (hasTasks) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlyCalendar(
    selectedDate: Long,
    pendingTasks: List<TaskPresentation>,
    onDateSelected: (Long) -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    // Fecha seleccionada
    val selectedCalendar = Calendar.getInstance().apply {
        timeInMillis = selectedDate
    }

    val currentMonth = selectedCalendar.get(Calendar.MONTH)
    val currentYear = selectedCalendar.get(Calendar.YEAR)

    // Primer día del mes
    val firstDayCalendar = Calendar.getInstance().apply {
        clear()
        set(Calendar.YEAR, currentYear)
        set(Calendar.MONTH, currentMonth)
        set(Calendar.DAY_OF_MONTH, 1)
    }

    // Día de la semana en que inicia el mes (0 = domingo)
    val firstDayOfWeek = firstDayCalendar.get(Calendar.DAY_OF_WEEK) - 1

    // Número de días del mes
    val daysInMonth = firstDayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Lista con espacios vacíos iniciales + días del mes
    val calendarCells: List<Int?> =
        List(firstDayOfWeek) { null } +
                (1..daysInMonth).map { it }

    // Nombre del mes
    val monthName = SimpleDateFormat(
        "MMMM",
        Locale.getDefault()
    ).format(Date(selectedDate)).replaceFirstChar { it.uppercase() }

    // Mostrar año solo si es distinto al actual
    val currentYearNow = Calendar.getInstance().get(Calendar.YEAR)

    val monthTitle =
        if (currentYear == currentYearNow) {
            monthName
        } else {
            "$monthName $currentYear"
        }

    // Días de la semana
    val weekDays = listOf("D", "L", "M", "M", "J", "V", "S")

    // Fecha actual del usuario
    val today = Calendar.getInstance()

    // Swipe horizontal para cambiar de mes
    var offsetX by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .pointerInput(selectedDate) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        offsetX += dragAmount
                    },
                    onDragEnd = {
                        when {
                            offsetX < -100f -> {
                                val nextMonth = Calendar.getInstance().apply {
                                    timeInMillis = selectedDate
                                    add(Calendar.MONTH, 1)
                                }
                                onDateSelected(nextMonth.timeInMillis)
                            }

                            offsetX > 100f -> {
                                val previousMonth = Calendar.getInstance().apply {
                                    timeInMillis = selectedDate
                                    add(Calendar.MONTH, -1)
                                }
                                onDateSelected(previousMonth.timeInMillis)
                            }
                        }

                        offsetX = 0f
                    }
                )
            }
    ) {

        // Título del mes
        Text(
            text = monthTitle,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            color = textColor
        )

        // Encabezado de días de la semana
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            userScrollEnabled = false,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            items(weekDays) { day ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelMedium,
                        color = textColor
                    )
                }
            }
        }

        // Calendario
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {

            items(calendarCells) { day ->

                if (day == null) {
                    Spacer(
                        modifier = Modifier.aspectRatio(1f)
                    )
                } else {
                    val dateCalendar = Calendar.getInstance().apply {
                        clear()
                        set(Calendar.YEAR, currentYear)
                        set(Calendar.MONTH, currentMonth)
                        set(Calendar.DAY_OF_MONTH, day)
                    }

                    val dateMillis = dateCalendar.timeInMillis

                    val isToday =
                        today.get(Calendar.YEAR) == currentYear &&
                                today.get(Calendar.MONTH) == currentMonth &&
                                today.get(Calendar.DAY_OF_MONTH) == day

                    val isSelected =
                        Calendar.getInstance().apply {
                            timeInMillis = selectedDate
                        }.let {
                            it.get(Calendar.YEAR) == currentYear &&
                                    it.get(Calendar.MONTH) == currentMonth &&
                                    it.get(Calendar.DAY_OF_MONTH) == day
                        }

                    val backgroundColor =
                        when {
                            isSelected -> colors.highlight
                            else -> Color.Transparent
                        }

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(backgroundColor)
                            .border(
                                width = if (isToday) 2.dp else 0.dp,
                                color = if (isToday) colors.highlight else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                onDateSelected(dateMillis)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskListSection(
    tasks: List<TaskPresentation>,
    isMonthly: Boolean,
    onEdit: (TaskPresentation) -> Unit,
    onComplete: (TaskPresentation) -> Unit
) {
    if (tasks.isEmpty()) {

        EmptyTasksState(isMonthly)

        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = tasks,
            key = { it.id }
        ) { task ->
            TaskRow(
                task = task,
                isMonthly = isMonthly,
                onEdit = onEdit,
                onComplete = onComplete
            )
        }
    }
}

@Composable
fun EmptyTasksState(isMonthly: Boolean) {

    val text = if (isMonthly) {
        "No hay tareas en este mes"
    } else {
        "No hay tareas para este día"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TaskRow(
    task: TaskPresentation,
    isMonthly: Boolean,
    onEdit: (TaskPresentation) -> Unit,
    onComplete: (TaskPresentation) -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    val time = formatTime(task.dueDate)
    val day = DateUtils.formatDayNumber(task.dueDate)
    val month = DateUtils.formatMonthName(task.dueDate)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Hora
            Text(
                text = time,
                style = MaterialTheme.typography.labelMedium,
                color = textColor
            )

            //Solo en vista mensual
            if (isMonthly) {
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )

                Text(
                    text = month.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        TaskItemCard(
            task = task,
            onEditClick = { onEdit(task) },
            onComplete = { onComplete(task) }
        )
    }
}

fun formatTime(time: Long): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault())
        .format(Date(time))
}

@Composable
fun TaskItemCard(
    task: TaskPresentation,
    onEditClick: () -> Unit,
    onComplete: (TaskPresentation) -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    var offsetX by remember(task.id) { mutableStateOf(0f) }
    val maxSwipe = -150f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {

        // Fondo (check)
        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topEnd = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
                    .background(colors.highlight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )
            }
        }

        // Card
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(colors.secondaryBg)
                .pointerInput(task.id) {
                    detectHorizontalDragGestures(

                        onDragEnd = {
                            if (offsetX < maxSwipe * 0.6f) {
                                onComplete(task)
                            }

                            // reset visual
                            offsetX = 0f
                        },

                        onHorizontalDrag = { _, dragAmount ->
                            offsetX = (offsetX + dragAmount)
                                .coerceIn(maxSwipe, 0f)
                        }
                    )
                }
                .padding(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        task.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = textColor
                    )

                    Text(
                        task.description,
                        style = MaterialTheme.typography.labelMedium,
                        color = textColor
                    )
                }

                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = textColor
                    )
                }
            }
        }
    }
}
@Composable
fun TaskDialog(
    mode: TaskDialogMode,
    task: TaskPresentation?,
    onSave: (title: String, description: String, date: Long, time: String) -> Unit,
    onDelete: () -> Unit,
    onConfirmComplete: () -> Unit,
    onDismiss: () -> Unit
) {

    val isEditable = mode != TaskDialogMode.COMPLETE

    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var selectedDate by remember {
        mutableStateOf(task?.dueDate ?: DateUtils.todayStart())
    }

    var showInvalidDateSnackbar by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {

        var selectedDateMillis by remember { mutableStateOf(task?.dueDate ?: System.currentTimeMillis()) }
        var selectedTimeText by remember { mutableStateOf(task?.time ?: "") }

        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = when (mode) {
                        TaskDialogMode.CREATE -> stringResource(R.string.Dialog_taks1)
                        TaskDialogMode.EDIT -> stringResource(R.string.Dialog_taks1)
                        TaskDialogMode.COMPLETE -> stringResource(R.string.Dialog_task2)
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppTextField(
                    value = title,
                    onValueChange = { if (isEditable) title = it },
                    hint = stringResource(R.string.text_field_task1),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                AppTextField(
                    value = description,
                    onValueChange = { if (isEditable) description = it },
                    hint = stringResource(R.string.text_field_task2),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    minLines = 2,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    DateButton(
                        text = DateUtils.formatDayNumber(selectedDate),
                        enabled = isEditable,
                        onClick = { showDatePicker = true }
                    )

                    TimeButton(
                        text = formatTime(selectedDate),
                        enabled = isEditable,
                        onClick = { showTimePicker = true }
                    )
                }

                if (showDatePicker) {
                    AppDatePickerDialog(
                        onDismiss = { showDatePicker = false },
                        onDateSelected = { millis ->

                            millis?.let { safeMillis ->

                                val newDate = Calendar.getInstance().apply {
                                    timeInMillis = safeMillis
                                }

                                val calendar = Calendar.getInstance().apply {
                                    timeInMillis = selectedDateMillis

                                    set(Calendar.YEAR, newDate.get(Calendar.YEAR))
                                    set(Calendar.MONTH, newDate.get(Calendar.MONTH))
                                    set(Calendar.DAY_OF_MONTH, newDate.get(Calendar.DAY_OF_MONTH))
                                }

                                selectedDateMillis = calendar.timeInMillis
                            }
                        }
                    )
                }

                if (showTimePicker) {
                    AppTimePickerDialog(
                        onDismiss = { showTimePicker = false },
                        onTimeSelected = { hour, minute ->

                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = selectedDateMillis
                                set(Calendar.HOUR_OF_DAY, hour)
                                set(Calendar.MINUTE, minute)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }

                            selectedDateMillis = calendar.timeInMillis

                            selectedTimeText = SimpleDateFormat(
                                "hh:mm a",
                                Locale.getDefault()
                            ).format(Date(selectedDateMillis))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                val onSaveTask = {
                    if (selectedDateMillis <= System.currentTimeMillis()) {
                        showInvalidDateSnackbar = true
                    } else {
                        onSave(
                            title,
                            description,
                            selectedDateMillis,
                            selectedTimeText
                        )
                    }
                }

                if (showInvalidDateSnackbar) {
                    Snackbar(
                        action = {
                            TextButton(
                                onClick = {
                                    showInvalidDateSnackbar = false
                                }
                            ) {
                                Text("OK")
                            }
                        }
                    ) {
                        Text("La fecha y hora seleccionadas deben ser futuras.")
                    }
                }

                val onDeleteTask = {
                    onDelete()
                }

                val onCompleteTask = {
                    onConfirmComplete()
                }

                DialogButtons(
                    mode = mode,
                    onSave = { onSaveTask() },
                    onDelete = { onDeleteTask() },
                    onConfirmComplete = { onCompleteTask() },
                    onDismiss = onDismiss
                )
            }
        }
    }
}
@Composable
fun DateButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    SizeButton(
        text = text,
        isSelected = enabled,
        onClick = onClick,
        modifier = Modifier.width(90.dp)
    )
}
@Composable
fun TimeButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    SizeButton(
        text = text,
        isSelected = enabled,
        onClick = onClick,
        modifier = Modifier.width(90.dp)
    )
}
@Composable
fun DialogButtons(
    mode: TaskDialogMode,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onConfirmComplete: () -> Unit,
    onDismiss: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        when (mode) {

            TaskDialogMode.COMPLETE -> {
                SizeButton("Cancelar", false, onDismiss)
                Spacer(modifier = Modifier.width(8.dp))
                SizeButton("Confirmar", true, onConfirmComplete)
            }

            else -> {

                if (mode == TaskDialogMode.EDIT) {
                    Text(
                        text = "Eliminar",
                        color = Color.Red,
                        modifier = Modifier.clickable { onDelete() }
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                SizeButton("Cancelar", false, onDismiss)

                Spacer(modifier = Modifier.width(8.dp))

                SizeButton("Guardar", true, onSave)
            }
        }
    }
}
enum class TaskDialogMode {
    CREATE,
    EDIT,
    COMPLETE
}

fun TaskPresentation.toDomain(): TaskItem {
    return TaskItem(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        dueTimeLabel = time,
        isCompleted = isCompleted,
        reminderEnabled = reminderEnabled,
        createdAt = createdAt
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (Long?) -> Unit
) {
    val colors = LocalZenithColors.current

    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {

        DatePicker(
            state = datePickerState,

            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,

                selectedDayContainerColor = colors.highlight,
                todayDateBorderColor = colors.highlight,

                selectedDayContentColor = colors.autoText(colors.highlight),
                dayContentColor = colors.autoText(MaterialTheme.colorScheme.surface),

                titleContentColor = colors.autoText(MaterialTheme.colorScheme.surface),
                headlineContentColor = colors.autoText(MaterialTheme.colorScheme.surface)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit
) {

    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    val state = rememberTimePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected(state.hour, state.minute)
                onDismiss()
            }) {
                Text("OK", color = colors.highlight)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = colors.highlight)
            }
        },
        text = {
            TimeInput(
                state = state,
                colors = TimePickerDefaults.colors(
                    selectorColor = colors.highlight,
                    containerColor = MaterialTheme.colorScheme.surface,
                    clockDialColor = MaterialTheme.colorScheme.surface,
                    clockDialSelectedContentColor = textColor,
                    clockDialUnselectedContentColor = textColor,
                    periodSelectorBorderColor = colors.highlight,
                    periodSelectorSelectedContainerColor = colors.highlight,
                    periodSelectorSelectedContentColor = textColor,
                    timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.surface,
                    timeSelectorSelectedContentColor = textColor,
                    timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = textColor
    )
}