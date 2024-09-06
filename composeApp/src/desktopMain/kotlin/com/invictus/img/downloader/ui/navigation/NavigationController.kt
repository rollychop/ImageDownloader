package com.invictus.img.downloader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

import com.invictus.img.downloader.ui.navigation.screen.ScreenRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator

sealed class NavigationAction {
    data object NavUp : NavigationAction()
    class Navigate(val screenRoute: ScreenRoute) : NavigationAction()
    class ChangeGraph(val fromScreenRoute: ScreenRoute, val toScreenRoute: ScreenRoute) :
        NavigationAction()
}

fun interface OnScreenAction {
    operator fun invoke(action: NavigationAction)
}


@Composable
fun rememberNavigationController(
    controller: Navigator,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): NavigationController = remember(coroutineScope, controller) {
    NavigationController(coroutineScope, controller)
}


class NavigationController(
    private val coroutineScope: CoroutineScope,
    val controller: Navigator,
) {


    private var currentRoute: String? = null

    init {
        coroutineScope.launch {
            controller.currentEntry.collect { entry ->
                currentRoute = entry?.route?.route
            }
        }
    }


    fun handleScreenAction(
        action: NavigationAction
    ) {

        if (action is NavigationAction.Navigate
            && action.screenRoute.route == currentRoute
        ) return

        coroutineScope.launch {
            when (action) {
                NavigationAction.NavUp -> controller.goBack()
                is NavigationAction.Navigate -> when (action.screenRoute) {
                    else -> {
                        controller.navigate(action.screenRoute.route)
                    }
                }

                is NavigationAction.ChangeGraph -> {
                    controller.navigate(
                        action.toScreenRoute.route, NavOptions(
                            popUpTo = PopUpTo(action.fromScreenRoute.route)
                        )
                    )
                }
            }
        }

    }

}
