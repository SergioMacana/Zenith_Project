package com.example.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

@Composable
fun AboutSection() {

    val uriHandler = LocalUriHandler.current

    val annotatedText = buildAnnotatedString {
        append("Acerca de Zenith\n")

        pushStringAnnotation(
            tag = "URL",
            annotation = "https://github.com/"
        )

        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Ir al repositorio")
        }

        pop()
    }

    ClickableText(
        text = annotatedText,
        style = MaterialTheme.typography.labelSmall,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = "URL",
                start = offset,
                end = offset
            ).firstOrNull()?.let { annotation ->
                uriHandler.openUri(annotation.item)
            }
        }
    )
}