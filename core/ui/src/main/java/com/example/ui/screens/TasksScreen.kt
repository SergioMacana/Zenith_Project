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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ui.R
import com.example.ui.components.AppFab
import com.example.ui.components.AppTextField
import com.example.ui.components.BaseSwitchScreen
import com.example.ui.components.SizeButton
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksScreen(
    onBack: () -> Unit
){
    var selectedIndex by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMode by remember { mutableStateOf<TaskDialogMode>(TaskDialogMode.CREATE) }
    var currentTask by remember { mutableStateOf<TaskUi?>(null) }

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
            onDismiss = { showDialog = false }
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskContentPager(
    selectedIndex: Int,
    onPageChanged: (Int) -> Unit,
    onEdit: (TaskUi) -> Unit,
    onComplete: (TaskUi) -> Unit

) {
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

    HorizontalPager(
        state = pagerState
    ) { page ->

        when (page) {
            0 -> WeeklyTasksScreen()
            1 -> MonthlyTasksScreen()
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyTasksScreen() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMode by remember { mutableStateOf<TaskDialogMode>(TaskDialogMode.CREATE) }

    var currentTask by remember { mutableStateOf<TaskUi?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        WeeklyCalendar(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskListSection(
            tasks = fakeTasks,
            isMonthly = false,

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
            onDismiss = { showDialog = false }
        )
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlyTasksScreen() {

    var showDialog by remember { mutableStateOf(false) }
    var dialogMode by remember { mutableStateOf(TaskDialogMode.CREATE) }

    var currentTask by remember { mutableStateOf<TaskUi?>(null) }

    Column {
        MonthlyCalendar()

        Spacer(modifier = Modifier.height(16.dp))

        TaskListSection(
            tasks = fakeTasks,
            isMonthly = false,

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
            onDismiss = { showDialog = false }
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    val today = LocalDate.now()
    val startOfWeek = today.with(DayOfWeek.MONDAY)
    val daysOfWeek = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(daysOfWeek) { date ->

            val isToday = date == today
            val isSelected = date == selectedDate

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
                    .clickable { onDateSelected (date) },
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )

                    Text(
                        text = date.dayOfWeek.name.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelMedium,
                        color = textColor
                    )
                }

                if (isToday) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 6.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlyCalendar() {
    val today = LocalDate.now()

    val month = Month.DECEMBER
    val year = today.year

    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val daysInMonth = month.length(firstDayOfMonth.isLeapYear)

    val days = (1..daysInMonth).map { day ->
        LocalDate.of(year, month, day)
    }
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Diciembre",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            color = textColor
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(days) { date ->

                val isToday = date == today

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = if (isToday) 2.dp else 0.dp,
                            color = if (isToday) colors.highlight else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
fun TaskListSection(
    tasks: List<TaskUi>,
    isMonthly: Boolean,
    onEdit: (TaskUi) -> Unit,
    onComplete: (TaskUi) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(tasks) { task ->
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
fun TaskRow(
    task: TaskUi,
    isMonthly: Boolean,
    onEdit: (TaskUi) -> Unit,
    onComplete: (TaskUi) -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Column(
            modifier = Modifier
                .width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text= task.time,
                style = MaterialTheme.typography.labelMedium,
                color = textColor
            )

            if (isMonthly) {
                Text(task.day ?: "", style = MaterialTheme.typography.bodyMedium, color = textColor)
                Text(task.month ?: "", style = MaterialTheme.typography.bodyMedium, color = textColor)
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

@Composable
fun TaskItemCard(
    task: TaskUi,
    onEditClick: () -> Unit,
    onComplete: (TaskUi) -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.background)

    var offsetX by remember { mutableStateOf(0f) }
    val maxSwipe = -150f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {

        Box(
            modifier = Modifier
                .matchParentSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
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

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(colors.secondaryBg)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            offsetX = if (offsetX < maxSwipe / 2) maxSwipe else 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX = (offsetX + dragAmount).coerceIn(maxSwipe, 0f)
                        }
                    )
                }
                .clickable { onComplete(task)}
                .padding(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(task.title, style = MaterialTheme.typography.titleLarge, color = textColor)
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
    task: TaskUi?,
    onDismiss: () -> Unit
) {

    val isEditable = mode != TaskDialogMode.COMPLETE

    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {

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
                        text = task?.day ?: "Seleccionar fecha",
                        enabled = isEditable,
                        onClick = { showDatePicker = true }
                    )

                    TimeButton(
                        text = task?.time ?: "Seleccionar hora",
                        enabled = isEditable,
                        onClick = { showTimePicker = true }
                    )
                }

                if (showDatePicker) {
                    AppDatePickerDialog(
                        onDismiss = { showDatePicker = false },
                        onDateSelected = {
                            // actualizar estado
                        }
                    )
                }

                if (showTimePicker) {
                    AppTimePickerDialog(
                        onDismiss = { showTimePicker = false },
                        onTimeSelected = { hour, minute ->
                            // actualizar estado
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))


                DialogButtons(
                    mode = mode,
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
                SizeButton("Confirmar", true, onDismiss)
            }

            else -> {
                Text(
                    text = "Eliminar",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable { }
                )

                Spacer(modifier = Modifier.width(8.dp))

                SizeButton("Cancelar", false, onDismiss)

                Spacer(modifier = Modifier.width(8.dp))

                SizeButton("Guardar", true, onDismiss)
            }
        }
    }
}
enum class TaskDialogMode {
    CREATE,
    EDIT,
    COMPLETE
}

data class TaskUi(
    val time: String,
    val title: String,
    val description: String,
    val day: String? = null,
    val month: String? = null
)

val fakeTasks = listOf(
    TaskUi(
        time = "07:00 am",
        title = "Beber agua",
        description = "Mantente hidratado durante el día",
        day = "20",
        month = "Diciembre"
    ),
    TaskUi(
        time = "02:00 pm",
        title = "Ir de compras",
        description = "No olvidar la comida de Mike",
        day = "24",
        month = "Diciembre"
    ),
    TaskUi(
        time = "04:30 pm",
        title = "Preparar cumple de Andrés",
        description = "Decidir si comprar o preparar la comida",
        day = "28",
        month = "Diciembre"
    )
)
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