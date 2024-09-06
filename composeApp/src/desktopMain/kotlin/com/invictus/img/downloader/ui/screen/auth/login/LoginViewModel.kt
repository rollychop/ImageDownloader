package com.invictus.img.downloader.ui.screen.auth.login

import com.invictus.img.downloader.di.Di
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope


class LoginViewModel : ViewModel() {


    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    val inputState = LoginInputState()

    val logInUseCase = Di.UseCase.loginUseCase


    fun login(onLoggedIn: () -> Unit) {
        val username = inputState.usernameState.text.uppercase()
        val password = inputState.passwordState.text
        viewModelScope.launch {
            _state.update { s -> s.copy(loading = true) }
            logInUseCase(username, password).fold(
                onSuccess = {
                    _state.update { s -> s.copy(loading = false) }
                    onLoggedIn()
                },
                onFailure = {
                    _state.update { s -> s.copy(loading = false, error = "${it.message}") }
                }
            )

        }
    }

    fun logout() {
        viewModelScope.launch {
            Di.UseCase.logoutUseCase()
        }
    }
}
