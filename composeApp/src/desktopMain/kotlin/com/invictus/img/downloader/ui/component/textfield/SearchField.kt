package com.invictus.img.downloader.ui.component.textfield

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import jdk.internal.org.jline.utils.Colors.h
import kotlinx.coroutines.delay

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit = {},
    active: Boolean = false,
    onActiveChange: (Boolean) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    modifier: Modifier = Modifier,
) {
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val rotation by rememberInfiniteTransition("rotation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(5000))
    )

    val pc = MaterialTheme.colorScheme.primary
    val sc = MaterialTheme.colorScheme.secondary
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .height(64.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { if (it.isFocused) onActiveChange(true) }
            .onKeyEvent {
                if (it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                    onActiveChange(false)
                    true
                } else {
                    false
                }
            }
            .padding(2.dp)
            .drawWithCache {
                val brush = Brush.verticalGradient(listOf(pc, sc))
                onDrawBehind {
                    this.rotate(rotation) {
                        drawRoundRect(
                            brush = brush,
                            cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx())
                        )
                    }
                }
            }
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.extraLarge)
            .padding(16.dp),
        textStyle = LocalTextStyle.current.copy(color = Color.Transparent),
        cursorBrush = SolidColor(Color.Transparent),
        decorationBox = { _ ->
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = "Search (e.g., id:123 or name:some_name)",
                        color = LocalContentColor.current.copy(.5f),
                        modifier = Modifier
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        runCatching {
                            val tokens = query.split(" ")

                            tokens.forEach { token ->
                                when {
                                    token.equals("and", ignoreCase = true) || token.equals(
                                        "or",
                                        ignoreCase = true
                                    ) || token.equals("all", ignoreCase = true) -> {
                                        withStyle(
                                            style = SpanStyle(
                                                color = Color.Blue,
                                                textDecoration = TextDecoration.Underline,
                                                fontWeight = FontWeight.Bold
                                            )
                                        ) {
                                            append(token)
                                        }
                                        append(" ")
                                    }

                                    token.contains(":") -> {
                                        val (key, value) = token.split(":", limit = 2)
                                        withStyle(
                                            style = SpanStyle(
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        ) {
                                            append("$key:")
                                        }
                                        withStyle(style = SpanStyle(color = Color.DarkGray)) {
                                            append("$value ")
                                        }
                                    }

                                    else -> {
                                        append("$token ")
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                )

            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        ),
        interactionSource = interactionSource
    )


    val shouldClearFocus = !active && isFocused
    LaunchedEffect(active) {
        if (shouldClearFocus) {
            delay(100.toLong())
            focusManager.clearFocus()
        }
    }
}
