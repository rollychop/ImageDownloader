package com.invictus.img.downloader.ui.screen.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.invictus.img.downloader.ui.navigation.OnScreenAction

@Composable
fun LoadingScreen(
    onScreenAction: OnScreenAction,
    viewModel: LoadingViewModel = moe.tlaster.precompose.viewmodel.viewModel { LoadingViewModel() },
) {
    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.load(onScreenAction)
    }
}
