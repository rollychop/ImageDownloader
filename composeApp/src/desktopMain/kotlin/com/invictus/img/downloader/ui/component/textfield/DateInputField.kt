package com.invictus.img.downloader.ui.component.textfield

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInputField(
    label: String,
    placeholder: String,
    inputState: DataTextFieldState<Long>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val fm = LocalFocusManager.current

    InputField(
        textFieldState = inputState,
        label = label,
        placeholder = placeholder,
        modifier = modifier
            .onFocusChanged {
                if (it.isFocused) {
                    showDatePickerDialog = true
                }
            },
        enabled = enabled,
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = ""
            )
        }
    )

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    try {
                        datePickerState.selectedDateMillis?.let { millis ->
                            inputState.updateData(
                                millis, Instant.ofEpochMilli(millis)
                                    .atZone(ZoneOffset.UTC)
                                    .toLocalDate()
                                    .format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))
                            )

                        }
                    } catch (_: Exception) {
                    }
                    fm.clearFocus()
                    showDatePickerDialog = false
                }) {
                    Text(text = "Select")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
            )
        }
    }


}
