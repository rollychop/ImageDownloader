package com.invictus.img.downloader.ui.component.text

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun BoxScope.ErrorText(
    errorMessage: String,
    @DrawableRes imageRes: DrawableResource? = null,
    withImage: Boolean = true
) {
    AnimatedVisibility(
        modifier = Modifier.align(Alignment.Center),
        visible = errorMessage.isNotBlank(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (withImage && imageRes != null) {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = "error image"
                )
            }
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ColumnScope.ErrorText(
    errorMessage: String,
) {
    AnimatedVisibility(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        visible = errorMessage.isNotBlank()
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}
