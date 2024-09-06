package com.invictus.img.downloader

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.invictus.img.downloader.di.Di
import com.invictus.img.downloader.ui.MainScreenApp
import imagedownloader.composeapp.generated.resources.Res
import imagedownloader.composeapp.generated.resources.compose_multiplatform
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.batikSvgDecoder
import io.kamel.image.config.resourcesFetcher
import moe.tlaster.precompose.ProvidePreComposeLocals
import org.jetbrains.compose.resources.painterResource

fun main() = application {

    val desktopConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()
        batikSvgDecoder()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "ImageDownloader",
        icon = painterResource(Res.drawable.compose_multiplatform)
    ) {
        ProvidePreComposeLocals {
            CompositionLocalProvider(LocalKamelConfig provides desktopConfig) {
                MainScreenApp()
            }
        }

        DisposableEffect(Unit) {


            onDispose { Di.dispose() }
        }
    }
}
