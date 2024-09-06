package com.invictus.img.downloader.domain.model.dialog

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

object DialogManager {

    private val _dialogs: MutableStateFlow<List<DialogData>> = MutableStateFlow(emptyList())
    val dialogs: StateFlow<List<DialogData>> get() = _dialogs.asStateFlow()

    fun showDialog(
        title: String,
        messageTop: String? = null,
        imageLink: String? = null,
        messageBottom: String? = null,
        actionName: String? = null,
        dismissName: String? = null,
        onAction: () -> Unit = {},
        onDismiss: (() -> Unit)? = null,
    ) {
        _dialogs.update { currentMessages ->
            currentMessages + DialogData(
                title = title,
                messageTop = messageTop,
                imageLink = imageLink,
                messageBottom = messageBottom,
                actionName = actionName,
                dismissName = dismissName,
                onAction = onAction,
                onDismiss = onDismiss,
                id = UUID.randomUUID().mostSignificantBits
            )
        }
    }

    fun setDialogShown(dialogId: Long) {
        _dialogs.update { currentDialog ->
            currentDialog.filterNot { it.id == dialogId }
        }
    }
}
