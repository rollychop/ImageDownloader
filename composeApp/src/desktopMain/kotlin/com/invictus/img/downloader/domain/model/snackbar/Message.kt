package com.invictus.img.downloader.domain.model.snackbar

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

sealed interface Message {
    val id: Long

    data class StringMessage(
        override val id: Long,
        val message: String,
    ) : Message

    data class ResourceMessage(
        override val id: Long,
        @StringRes val message: Int,
        val args: List<Any> = emptyList(),
    ) : Message

    companion object {
        fun from(text: String): Message {
            return StringMessage(
                id = UUID.randomUUID().mostSignificantBits,
                message = text
            )
        }

        fun from(@StringRes message: Int): Message {
            return ResourceMessage(
                message = message,
                id = UUID.randomUUID().mostSignificantBits
            )
        }

        fun from(@StringRes message: Int, vararg args: Any): Message {
            return ResourceMessage(
                message = message,
                id = UUID.randomUUID().mostSignificantBits,
                args = args.toList()
            )
        }
    }
}

/**
 * Class responsible for managing Snackbar messages to show on the screen
 */
object SnackbarManager {

    private val _messages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages.asStateFlow()

    fun showMessage(message: Message) {
        _messages.update { currentMessages ->
            currentMessages + message
        }
    }

    fun setMessageShown(messageId: Long) {
        _messages.update { currentMessages ->
            currentMessages.filterNot { it.id == messageId }
        }
    }
}
