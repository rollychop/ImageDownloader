package com.invictus.img.downloader.ui.screen.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.invictus.img.downloader.ui.component.textfield.PasswordState
import com.invictus.img.downloader.ui.component.textfield.TextFieldState

class LoginInputState {

    val passwordState = PasswordState()
    val usernameState = TextFieldState(
        validator = {
            it.length > 5
        },
        errorFor = {
            "Username is too short"
        }
    )

    var forAccepted: Boolean by mutableStateOf(false)
        private set

    fun onAccepted(checked: Boolean) {
        forAccepted = checked
    }


}
