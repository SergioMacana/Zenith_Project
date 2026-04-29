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
import androidx.compose.runtime.collectAsState
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
import com.example.domain.catalog.MoodEmoji
import com.example.domain.catalog.MoodEmojiCatalog
import com.example.domain.model.MoodMosaicItem
import com.example.ui.R
import com.example.ui.components.AppTextField
import com.example.ui.components.BaseSwitchScreen
import com.example.ui.components.SizeButton
import com.example.ui.theme.LocalZenithColors
import com.example.ui.theme.autoText
import com.example.ui.viewmodel.MoodViewModel
import com.example.ui.viewmodel.SettingsViewModel

@Composable
fun MoodScreen(
    moodViewModel: MoodViewModel,
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val settingsState by settingsViewModel.settingsState.collectAsState()

    var selectedIndex by remember { mutableStateOf(0) }

    val titles = listOf(
        "${stringResource(R.string.title_moody_1)} ${settingsState.userName.ifBlank { "Usuario" }}?",
        "${stringResource(R.string.title_moody_2)} ${settingsState.userName.ifBlank { "Usuario" }}"
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
            onPageChanged = { selectedIndex = it },
            onSaveMood = { moodId, moodLabel, note ->
                moodViewModel.saveMood(
                    moodId = moodId,
                    moodLabel = moodLabel,
                    note = note
                )
            },
            getMosaicForPeriod = { period ->
                moodViewModel.getMosaicForPeriod(period)
            }
        )
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoodContentPager(
    selectedIndex: Int,
    onPageChanged: (Int) -> Unit,
    onSaveMood: (String, String, String) -> Unit,
    getMosaicForPeriod: (String) -> List<MoodMosaicItem>
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
            0 -> MoodGridScreen(
                onSaveMood = onSaveMood
            )
            1 -> MoodHistoryScreen(
                getMosaicForPeriod = getMosaicForPeriod
            )
        }
    }
}

@Composable
fun MoodGridScreen(
    onSaveMood: (moodId: String, moodLabel: String, note: String) -> Unit
) {
    var selectedMood by remember { mutableStateOf<MoodEmoji?>(null) }
    var note by remember { mutableStateOf("") }

    val moods = MoodEmojiCatalog.moods

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(moods) { mood ->
            MoodGridItem(
                label = mood.label,
                icon = moodDrawableFor(mood.id),
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
            },
            onSave = {
                onSaveMood(
                    mood.id,
                    mood.label,
                    note
                )
                selectedMood = null
                note = ""
            }
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoodHistoryScreen(
    getMosaicForPeriod: (String) -> List<MoodMosaicItem>
) {
    var selectedFilter by remember { mutableStateOf("day") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        HistoryFilter(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        val mosaicItems = getMosaicForPeriod(selectedFilter)

        //MOSAICO
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(-6.dp),
            verticalArrangement = Arrangement.spacedBy(-6.dp)
        ) {
            mosaicItems.shuffled().forEach { mood ->

                val randomOffset = (-6..6).random()
                Box(
                    modifier = Modifier.offset(y = randomOffset.dp)
                ) {
                    MoodBubble(
                        backgroundRes = R.drawable.empty_emoji,
                        label = mood.moodLabel,
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
        size >= 60.dp -> 12.sp
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
fun HistoryFilter(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {

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
                onClick = { onFilterSelected(filter) },
                modifier = Modifier.width(90.dp)
            )
        }
    }
}

@Composable
fun MoodDialog(
    mood: MoodEmoji,
    note: String,
    onNoteChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
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
                    painter = painterResource(moodDrawableFor(mood.id)),
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
                    onClick = onSave,
                    modifier = Modifier.width(120.dp)
                )
            }
        }
    }
}

fun moodDrawableFor(id: String): Int {
    return when (id) {
        "feliicidad" -> R.drawable.emoji_feliicidad
        "emoji_tristesa" -> R.drawable.emoji_tristesa
        "emoji_enojo" -> R.drawable.emoji_enojo
        "emoji_mudo" -> R.drawable.emoji_mudo
        "emoji_desinteres" -> R.drawable.emoji_desinteres
        "emoji_contento" -> R.drawable.emoji_contento
        "emoji_miedo" -> R.drawable.emoji_miedo
        "emoji_terror" -> R.drawable.emoji_terror
        "emoji_pensativo" -> R.drawable.emoji_pensativo
        "emoji_aburrido" -> R.drawable.emoji_aburrido
        "emoji_desconfianza" -> R.drawable.emoji_desconfianza
        "emoji_llanto" -> R.drawable.emoji_llanto
        "emoji_ira" -> R.drawable.emoji_ira
        "emoji_desesperanza" -> R.drawable.emoji_desesperanza
        "emoji_malicia" -> R.drawable.emoji_malicia
        "emoji_rencor" -> R.drawable.emoji_rencor
        "emoji_vengativo" -> R.drawable.emoji_vengativo
        "emoji_frustraci_n" -> R.drawable.emoji_frustraci_n
        "emoji_rechazo" -> R.drawable.emoji_rechazo
        "emoji_alegria" -> R.drawable.emoji_alegria
        "emoji_asco" -> R.drawable.emoji_asco
        "emoji_pereza" -> R.drawable.emoji_pereza
        "emoji_nerviosismo" -> R.drawable.emoji_nerviosismo
        "emoji_enamoramiento" -> R.drawable.emoji_enamoramiento
        "emoji_desiluci_n" -> R.drawable.emoji_desiluci_n
        "emoji_maldiciendo" -> R.drawable.emoji_maldiciendo
        "emoji_agradecimiento" -> R.drawable.emoji_agradecimiento
        "emoji_juicioso" -> R.drawable.emoji_juicioso
        "emoji_anonadado" -> R.drawable.emoji_anonadado
        "emoji_interes" -> R.drawable.emoji_interes
        else -> R.drawable.empty_emoji
    }
}

fun weightToSize(weight: Int): Dp {
    return when (weight) {
        in 9..10 -> 220.dp
        in 7..8 -> 180.dp
        in 5..6 -> 120.dp
        in 3..4 -> 80.dp
        else -> 60.dp
    }
}