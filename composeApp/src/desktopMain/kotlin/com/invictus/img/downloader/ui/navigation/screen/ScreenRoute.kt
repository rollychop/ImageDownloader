package com.invictus.img.downloader.ui.navigation.screen

sealed class Route(val route: String)

sealed class ScreenRoute(
    val route: String,
) {
    data object Loading : ScreenRoute("loading")
    data object Login : ScreenRoute("auth-route")
    data object Home : ScreenRoute("authenticated-route") {
        data object Dashboard : ScreenRoute("dashboard-route")
        data class Users(val prefix: String) : ScreenRoute("users/${prefix}") {
            companion object : Route("users/{prefix}")
        }
    }
}
