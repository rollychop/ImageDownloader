package com.invictus.img.downloader.domain.use_case.user

import com.invictus.img.downloader.data.UserDataStore
import com.invictus.img.downloader.domain.model.user.UserModel
import kotlinx.coroutines.flow.Flow

class GetLoggedUser(
    private val dataStore: UserDataStore
) {
    operator fun invoke(): Flow<UserModel> = dataStore.getUserFlow()
}
