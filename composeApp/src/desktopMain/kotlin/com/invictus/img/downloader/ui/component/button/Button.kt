package com.invictus.img.downloader.ui.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun InOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    borderBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    borderWith: Dp = 2.dp,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides if (enabled) LocalContentColor.current
        else LocalContentColor.current.copy(alpha = .7f)
    ) {
        Row(
            modifier = modifier
                .border(
                    brush = if (enabled) borderBrush
                    else SolidColor(MaterialTheme.colorScheme.outline),
                    width = borderWith,
                    shape = shape
                )
                .clip(shape)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
                .padding(contentPadding),
            content = content,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )


    }
}


@Composable
fun InTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = colors,
        enabled = enabled,
        content = content,
        modifier = modifier,
        contentPadding = contentPadding,
        shape = shape
    )
}

@Composable
fun InButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        content = {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onPrimary,
            ) {
                ProvideTextStyle(
                    value = MaterialTheme.typography.labelLarge
                ) {
                    content()
                }
            }
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(shape)
            .background(brush = backgroundBrush, alpha = if (enabled) 1f else .7f)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(contentPadding)
    )
}
