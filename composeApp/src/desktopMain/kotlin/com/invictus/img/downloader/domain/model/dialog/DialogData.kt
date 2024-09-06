package com.invictus.img.downloader.domain.model.dialog

data class DialogData(
    val id: Long,
    val title: String,
    val messageTop: String? = null,
    val imageLink: String? = null,
    val messageBottom: String? = null,
    val actionName: String? = null,
    val dismissName: String? = null,
    val onAction: () -> Unit = {},
    val onDismiss: (() -> Unit)? = null
)

