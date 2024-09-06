package com.invictus.img.downloader.ui.screen.home.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.invictus.img.downloader.ui.component.text.ErrorText
import com.invictus.img.downloader.ui.navigation.NavigationAction
import com.invictus.img.downloader.ui.navigation.OnScreenAction
import com.invictus.img.downloader.ui.navigation.screen.ScreenRoute
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.viewmodel.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onScreenAction: OnScreenAction,
    viewModel: DashboardViewModel = viewModel { DashboardViewModel() }
) {


    val state = viewModel.state.collectAsStateWithLifecycle().value
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Organisations")
                },
                actions = {
                    IconButton(onClick = {
                        onScreenAction(NavigationAction.ChangeGraph(ScreenRoute.Home, ScreenRoute.Login))
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "")
                    }
                }
            )
        }
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp),
            ) {
                item {
                    Row(
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceDim)
                            .height(IntrinsicSize.Max)
                    ) {
                        TableCell(text = "Prefix", weight = 1f, textAlign = TextAlign.Center)
                        TableCell(text = "Name", weight = 1f, textAlign = TextAlign.Center)
                        TableCell(text = "Address", weight = 1f, textAlign = TextAlign.Center)
                        TableCell(text = "Action", weight = 1f, textAlign = TextAlign.Center)
                    }
                }
                items(state.organisations) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
                    ) {
                        TableCell(text = it.prefix, weight = 1f)
                        TableCell(text = it.name, weight = 1f)
                        TableCell(text = it.address, weight = 1f)
                        TableCell(weight = 1f, modifier = Modifier.clickable {
                            onScreenAction(NavigationAction.Navigate(ScreenRoute.Home.Users(it.prefix)))
                        }) {
                            Icon(Icons.Default.PeopleAlt, "download images")
                        }
                    }
                }
            }
            if (state.loading) {
                CircularProgressIndicator()
            }
            ErrorText(state.error)
        }
    }


}

@Composable
fun RowScope.TableCell(
    text: String,
    modifier: Modifier = Modifier,
    weight: Float? = null,
    textAlign: TextAlign = TextAlign.Start,
) {
    Box(
        modifier.fillMaxHeight()
            .border(1.dp, Color.Black)
            .then(if (weight != null) Modifier.weight(weight) else Modifier)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = textAlign
        )
    }
}

@Composable
fun RowScope.TableCell(
    modifier: Modifier = Modifier,
    weight: Float? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier.fillMaxHeight()
            .border(1.dp, Color.Black)
            .then(if (weight != null) Modifier.weight(weight) else Modifier)
            .padding(8.dp),
        contentAlignment = Alignment.Center,
        content = content
    )
}
