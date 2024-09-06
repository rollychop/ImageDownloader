package com.invictus.img.downloader.domain.use_case.session

import com.invictus.img.downloader.data.UserDataStore
import com.invictus.img.downloader.domain.model.session.SessionRoute


class CheckLoggedInSession(
    private val userDataStore: UserDataStore,
) {
    suspend operator fun invoke(): SessionRoute {
        val isValid = userDataStore.getIcp() != null
        return SessionRoute(isValid)
    }

}
