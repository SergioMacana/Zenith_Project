package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BaseSwitchScreen(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
    extraFab: (@Composable (onClick: () -> Unit) -> Unit)? = null,
    onExtraFabClick: () -> Unit = {},
    content: @Composable (selectedIndex: Int) -> Unit
) {
    Scaffold(
        floatingActionButton = {

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                extraFab?.invoke(onExtraFabClick)

                AppFab(
                    icon = Icons.Default.ArrowBack,
                    onClick = onFabClick,
                    contentDescription = "Volver"
                )
        }
        }
    ) { padding ->

        Column(
            modifier = modifier
                .fillMaxSize()
        ) {

            AppHeader(
                title = title,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AppSegmentedControl(
                    options = options,
                    selectedIndex = selectedIndex,
                    onOptionSelected = onOptionSelected,
                    modifier = Modifier.width(180.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                content(selectedIndex)
            }
        }
    }
}