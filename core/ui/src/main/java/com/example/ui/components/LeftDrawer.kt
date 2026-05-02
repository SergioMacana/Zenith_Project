package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
@Composable
fun LeftDrawer(
    isOpen: Boolean,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    val width = LocalConfiguration.current.screenWidthDp.dp * 0.6f

    AnimatedVisibility(
        visible = isOpen,
        enter = slideInHorizontally { -it },
        exit = slideOutHorizontally { -it }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(width)
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
        ) {
            content()
        }
    }
}
