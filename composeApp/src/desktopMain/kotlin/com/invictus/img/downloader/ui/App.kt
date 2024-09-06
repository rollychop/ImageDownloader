package com.invictus.img.downloader.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.invictus.img.downloader.ui.navigation.rememberNavigationController
import com.invictus.img.downloader.ui.navigation.screen.ScreenRoute
import com.invictus.img.downloader.ui.screen.auth.login.LoginScreen
import com.invictus.img.downloader.ui.screen.auth.login.LoginViewModel
import com.invictus.img.downloader.ui.screen.home.dashboard.DashboardScreen
import com.invictus.img.downloader.ui.screen.home.users.UsersScreen
import com.invictus.img.downloader.ui.screen.loading.LoadingScreen
import com.invictus.img.downloader.ui.theme.ImageDownloaderTheme
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun MainScreenApp() {


    ImageDownloaderTheme {
        Surface {
            val controller = rememberNavigationController(
                rememberNavigator()
            )
            NavHost(
                modifier = Modifier,
                navigator = controller.controller,
                initialRoute = ScreenRoute.Loading.route
            ) {
                scene(ScreenRoute.Loading.route) {
                    LoadingScreen(
                        onScreenAction = controller::handleScreenAction,
                    )
                }

                scene(ScreenRoute.Login.route) {
                    LoginScreen(
                        onScreenAction = controller::handleScreenAction,
                        viewModel = viewModel { LoginViewModel() }
                    )
                }

                group(
                    route = ScreenRoute.Home.route,
                    initialRoute = ScreenRoute.Home.Dashboard.route
                ) {
                    scene(ScreenRoute.Home.Dashboard.route) {
                        DashboardScreen(
                            onScreenAction = controller::handleScreenAction
                        )
                    }
                    scene(ScreenRoute.Home.Users.route) { from ->
                        UsersScreen(
                            prefix = from.path("prefix", "") ?: "",
                            onScreenAction = controller::handleScreenAction,
                        )
                    }
                }
            }
        }
    }
}
