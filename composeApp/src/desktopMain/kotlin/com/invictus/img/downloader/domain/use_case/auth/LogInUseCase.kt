package com.invictus.img.downloader.domain.use_case.auth

import com.invictus.img.downloader.domain.repository.AuthRepository

class LogInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String) =
        authRepository.login(username, password)
}
