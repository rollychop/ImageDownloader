package com.invictus.img.downloader.data.repository

import com.invictus.img.downloader.data.UserDataStore
import com.invictus.img.downloader.data.mapper.user.toUserModel
import com.invictus.img.downloader.data.service.AuthService
import com.invictus.img.downloader.domain.model.user.UserModel
import com.invictus.img.downloader.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val ioDispatcher: CoroutineDispatcher,
    private val userDataStore: UserDataStore,
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String
    ): Result<UserModel> =
        kotlin.runCatching {
            withContext(ioDispatcher) {
                val split = username.split("-")
                userDataStore.savePrefixHeader(
                    when {
                        split.size == 2 -> split[0]
                        else -> "invictus"
                    }
                )
            }

            val response = authService.login(
                "{\"username\":\"$username\",\"password\":\"$password\"}"
                    .toRequestBody("application/json".toMediaTypeOrNull())
            )

            val cResponse = response.getBodyOrThrow()
            check(cResponse.logged == true) {
                cResponse.message ?: "Failed to login"
            }
            val model = response.getDataOrThrow().toUserModel()
            userDataStore.saveUserInfo(model)
            model
        }


    override suspend fun profile(): Result<UserModel> =
        runCatchingCustom {
            val response = authService.profile()
            val model = response.getDataOrThrow().toUserModel()
            userDataStore.saveUserInfo(model)
            model
        }

    override suspend fun register(
        email: String,
        password: String,
        name: String,
        username: String,
        mobileNumber: String,
    ): Result<UserModel> = runCatchingCustom {
        val response = authService.register(
            ("{\"email\":\"$email\"," +
                    "\"password\":\"$password\"," +
                    "\"mobile_no\":\"$mobileNumber\"," +
                    "\"name\":\"$name\",\"prefix\":\"$username\"}")
                .toJsonRequestBody()
        )
        val data = response.getDataOrThrow().toUserModel()
        data
    }

    override suspend fun forgetPassword(username: String)
            : Result<Any> = runCatchingCustom {
        throw IllegalStateException("Not implemented")
    }

    override suspend fun logout(): Result<Any> = runCatchingCustom {
        val response = authService.logout()
        val cResponse = response.getBodyOrThrow()
        if (cResponse.status == 401) {
            return@runCatchingCustom
        }
        check(cResponse.isSuccessful) { cResponse.message ?: "Failed to log out" }
    }
}
