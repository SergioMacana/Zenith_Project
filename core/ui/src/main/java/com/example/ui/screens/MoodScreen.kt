package com.example.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.R
import com.example.ui.components.AppTextField
import com.example.ui.components.BaseSwitchScreen
import com.example.ui.components.SizeButton
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText

@Composable
fun MoodScreen(
    onBack: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }

    val titles = listOf(
        stringResource(R.string.title_moody_1),
        stringResource(R.string.title_moody_2)
    )

    val options = listOf(stringResource(R.string.switch_opc1),
        stringResource(R.string.switch_opc2))

    BaseSwitchScreen(
        title = titles[selectedIndex],
        options = options,
        selectedIndex = selectedIndex,
        onOptionSelected = { selectedIndex = it },
        onFabClick = onBack
    ) {
        MoodContentPager(
            selectedIndex = selectedIndex,
            onPageChanged = { selectedIndex = it }
        )
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoodContentPager(
    selectedIndex: Int,
    onPageChanged: (Int) -> Unit
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
            0 -> MoodGridScreen()
            1 -> MoodHistoryScreen()
        }
    }
}

@Composable
fun MoodGridScreen() {
    var selectedMood by remember { mutableStateOf<MoodEmoji?>(null) }
    var note by remember { mutableStateOf("") }

    val moods = listOf(
        MoodEmoji(R.drawable.emoji_feliicidad, "Feliz"),
        MoodEmoji(R.drawable.emoji_tristesa, "Triste"),
        MoodEmoji(R.drawable.emoji_enojo, "Enojado"),
        MoodEmoji(R.drawable.emoji_mudo, "Mudo"),
        MoodEmoji(R.drawable.emoji_desinteres, "Desinteres"),
        MoodEmoji(R.drawable.emoji_contento, "Contento"),
        MoodEmoji(R.drawable.emoji_miedo, "Ansioso"),
        MoodEmoji(R.drawable.emoji_terror, "Asustado"),
        MoodEmoji(R.drawable.emoji_pensativo, "Pensativo"),
        MoodEmoji(R.drawable.emoji_aburrido, "Aburrido"),
        MoodEmoji(R.drawable.emoji_desconfianza, "Desconfianza"),
        MoodEmoji(R.drawable.emoji_llanto, "Llanto"),
        MoodEmoji(R.drawable.emoji_ira, "Estresado"),
        MoodEmoji(R.drawable.emoji_desesperanza, "Solo"),
        MoodEmoji(R.drawable.emoji_malicia, "Malicioso"),
        MoodEmoji(R.drawable.emoji_rencor, "Rencoroso"),
        MoodEmoji(R.drawable.emoji_vengativo, "Vengativo"),
        MoodEmoji(R.drawable.emoji_frustraci_n, "Frustrado"),
        MoodEmoji(R.drawable.emoji_rechazo, "Rechazo"),
        MoodEmoji(R.drawable.emoji_alegria, "Divertido"),
        MoodEmoji(R.drawable.emoji_asco, "Enfermo"),
        MoodEmoji(R.drawable.emoji_pereza, "Somnoliento"),
        MoodEmoji(R.drawable.emoji_nerviosismo, "Preocupado"),
        MoodEmoji(R.drawable.emoji_enamoramiento, "Romántico"),
        MoodEmoji(R.drawable.emoji_desiluci_n, "Desilucionado"),
        MoodEmoji(R.drawable.emoji_maldiciendo, "Maldiciendo"),
        MoodEmoji(R.drawable.emoji_agradecimiento, "Agradecido"),
        MoodEmoji(R.drawable.emoji_juicioso, "Juicioso"),
        MoodEmoji(R.drawable.emoji_anonadado, "Anonadado"),
        MoodEmoji(R.drawable.emoji_interes, "Interesado")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(moods) { mood ->
            MoodGridItem(
                label = mood.label,
                icon = mood.icon,
                onClick = {
                    selectedMood = mood
                }
            )
        }
    }
    selectedMood?.let { mood ->
        MoodDialog(
            mood = mood,
            note = note,
            onNoteChange = { note = it },
            onDismiss = {
                selectedMood = null
                note = ""
            }
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoodHistoryScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        HistoryFilter()

        Spacer(modifier = Modifier.height(16.dp))

        //MOSAICO
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(-6.dp),
            verticalArrangement = Arrangement.spacedBy(-6.dp)
        ) {
            fakeMoods.shuffled().forEach { mood ->

                val randomOffset = (-6..6).random()
                Box(
                    modifier = Modifier.offset(y = randomOffset.dp)
                ) {
                    MoodBubble(
                        backgroundRes = R.drawable.empty_emoji,
                        label = mood.label,
                        size = weightToSize(mood.weight)
                    )
                }
            }
        }
    }
}
@Composable
fun MoodGridItem(
    @DrawableRes icon: Int,
    label: String,
    onClick: () -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.surface)

    Column(
        modifier = Modifier
            .padding(12.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(icon),
            contentDescription = label,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
}

@Composable
fun MoodBubble(
    @DrawableRes backgroundRes: Int,
    label: String,
    size: Dp
) {
    val colors = LocalZenithColors.current

    val fontSize = when {
        size > 180.dp -> 36.sp
        size > 100.dp -> 24.sp
        size > 50.dp -> 18.sp
        else -> 9.sp
    }

    Box(
        modifier = Modifier
            .size(size)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = label,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = label,
            fontSize = fontSize,
            style = MaterialTheme.typography.labelMedium,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(6.dp)
        )
    }
}

@Composable
fun HistoryFilter() {

    var selectedFilter by remember { mutableStateOf("day") }

    val filters = listOf("day", "week", "month")

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        filters.forEach { filter ->
            SizeButton(
                text = stringResource(
                    id = when (filter) {
                        "day" -> R.string.opc_day
                        "week" -> R.string.opc_week
                        else -> R.string.opc_month
                    }
                ),
                isSelected = selectedFilter == filter,
                onClick = { selectedFilter = filter },
                modifier = Modifier.width(90.dp)
            )
        }
    }
}
data class MoodBubbleData(
    val label: String,
    val weight: Int
)

data class MoodEmoji(
    @DrawableRes val icon: Int,
    val label: String

)
@Composable
fun MoodDialog(
    mood: MoodEmoji,
    note: String,
    onNoteChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalZenithColors.current
    val textColor = colors.autoText(MaterialTheme.colorScheme.surface)

    Dialog(onDismissRequest = onDismiss) {

        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .width(280.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(R.string.Dialog_Moody),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(mood.icon),
                    contentDescription = mood.label,
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = mood.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppTextField(
                    value = note,
                    onValueChange = onNoteChange,
                    hint = stringResource(R.string.text_field_moody),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    minLines = 2,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(20.dp))

                SizeButton(
                    text = stringResource(R.string.button_save),
                    isSelected = true,
                    onClick = onDismiss,
                    modifier = Modifier.width(120.dp)
                )
            }
        }
    }
}

val fakeMoods = listOf(
    MoodBubbleData("Feliz", 10),
    MoodBubbleData("Tranquilo", 8),
    MoodBubbleData("Ansioso", 6),
    MoodBubbleData("Cansado", 5),
    MoodBubbleData("Motivado", 7),
    MoodBubbleData("Enojado", 3),
    MoodBubbleData("Pensativo", 4),
    MoodBubbleData("Agradecido", 2),
    MoodBubbleData("Aburrido", 1),
    MoodBubbleData("Concentrado", 6),
    MoodBubbleData("Estresado", 5),
    MoodBubbleData("Optimista", 7),
    MoodBubbleData("Asquiado", 3),
    MoodBubbleData("Frustrado", 4),
    MoodBubbleData("Desinteresado", 2),
    MoodBubbleData("Perezoso", 1),
    MoodBubbleData("Rencoroso", 6),
    MoodBubbleData("Interesado", 5)
)

fun weightToSize(weight: Int): Dp {
    return when (weight) {
        in 9..10 -> 220.dp
        in 7..8 -> 180.dp
        in 5..6 -> 100.dp
        in 3..4 -> 50.dp
        else -> 140.dp
    }
}