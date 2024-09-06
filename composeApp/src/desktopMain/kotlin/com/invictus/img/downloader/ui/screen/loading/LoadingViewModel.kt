package com.invictus.img.downloader.ui.screen.loading

import com.invictus.img.downloader.di.Di
import com.invictus.img.downloader.domain.use_case.session.CheckLoggedInSession
import com.invictus.img.downloader.ui.navigation.NavigationAction
import com.invictus.img.downloader.ui.navigation.OnScreenAction
import com.invictus.img.downloader.ui.navigation.screen.ScreenRoute
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class LoadingViewModel : ViewModel() {

    private val checkLoggedInSession: CheckLoggedInSession = Di.UseCase.checkLogInUseCase

    fun load(action: OnScreenAction) {
        viewModelScope.launch {
            val sessionRoute = checkLoggedInSession.invoke()
            if (sessionRoute.isLoggedIn) {
                action(
                    NavigationAction.ChangeGraph(
                        ScreenRoute.Loading,
                        ScreenRoute.Home
                    )
                )
            } else {
                action(
                    NavigationAction.ChangeGraph(
                        ScreenRoute.Loading,
                        ScreenRoute.Login
                    )
                )
            }
        }
    }

}
