package com.invictus.img.downloader.ui.screen.home.users

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.invictus.img.downloader.domain.model.user.IdCardUserModel
import com.invictus.img.downloader.ui.component.button.LoadingButton
import com.invictus.img.downloader.ui.component.button.LoadingButtonType
import com.invictus.img.downloader.ui.component.text.ErrorText
import com.invictus.img.downloader.ui.component.textfield.DateInputField
import com.invictus.img.downloader.ui.navigation.NavigationAction
import com.invictus.img.downloader.ui.navigation.OnScreenAction
import com.invictus.img.downloader.ui.screen.home.dashboard.TableCell
import imagedownloader.composeapp.generated.resources.Res
import imagedownloader.composeapp.generated.resources.no_image_placeholder
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.viewmodel.viewModel
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun UsersScreen(
    prefix: String,
    onScreenAction: OnScreenAction,
    viewModel: UsersViewModel = viewModel(listOf(prefix)) { UsersViewModel() }
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.load(prefix)
    }
    var showDownloadDialog by remember { mutableStateOf(false) }

    val selectedUsers = remember {
        mutableStateMapOf<IdCardUserModel, Boolean>()
    }


    val (startDate, endDate) = viewModel.startAndEndTextFieldState

    val scrollState = rememberLazyListState()
    val scrolled by remember { derivedStateOf { scrollState.firstVisibleItemIndex > 1 } }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = viewModel.filterQuery,
                onSearch = {},
                active = false,
                onActiveChange = {},
                onQueryChange = viewModel::filter,
                leadingIcon = {
                    IconButton(onClick = {
                        onScreenAction(NavigationAction.NavUp)
                    }) {
                        Icon(Icons.Default.ArrowBackIosNew, "")
                    }
                },
                trailingIcon = {
                    LoadingButton(
                        text = "Download Images",
                        onClick = {
                            showDownloadDialog = true
                        }
                    )
                },
                placeholder = {
                    Text("Search Users")
                }
            ) {}
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp),
            ) {
                stickyHeader(key = "header", contentType = "h-row") {
                    Row(
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceDim)
                            .height(IntrinsicSize.Max)
                    ) {
                        TableCell(
                            text = "Image",
                            modifier = Modifier.width(80.dp),
                            textAlign = TextAlign.Center
                        )
                        TableCell(text = "Name", weight = 1f, textAlign = TextAlign.Center)
                        TableCell(text = "Login ID", weight = 1f, textAlign = TextAlign.Center)
                        TableCell(text = "Display ID", weight = 1f, textAlign = TextAlign.Center)
                        TableCell(
                            modifier = Modifier,
                            weight = 1f
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text("Action")
                                TriStateCheckbox(
                                    state = when {
                                        selectedUsers.isEmpty() || state.filteredUsers.isEmpty() -> ToggleableState.Off
                                        selectedUsers.size == state.filteredUsers.size -> ToggleableState.On
                                        else -> ToggleableState.Indeterminate
                                    },
                                    onClick = {
                                        when {
                                            selectedUsers.isEmpty() && state.filteredUsers.isEmpty() -> Unit
                                            selectedUsers.size == state.filteredUsers.size -> {
                                                selectedUsers.clear()
                                            }

                                            else -> {
                                                selectedUsers.clear()
                                                state.filteredUsers.forEach {
                                                    selectedUsers[it] = true
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                items(state.filteredUsers, key = { it.id }, contentType = { "row" }) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
                    ) {
                        TableCell(modifier = Modifier.width(80.dp)) {
                            if (it.picture.startsWith("https://")) {
                                KamelImage(
                                    resource = if (it.picture.startsWith("https://")) asyncPainterResource(it.picture)
                                    else asyncPainterResource(Res.drawable.no_image_placeholder),
                                    contentDescription = "",
                                    modifier = Modifier.size(64.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop,

                                    )
                            } else {
                                Image(
                                    painter = painterResource(Res.drawable.no_image_placeholder),
                                    contentDescription = "",
                                    modifier = Modifier.size(64.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                        }
                        TableCell(text = it.fullName, weight = 1f)
                        TableCell(text = it.loginId, weight = 1f)
                        TableCell(text = it.displayId, weight = 1f)
                        TableCell(weight = 1f, modifier = Modifier.clickable {
                            if (selectedUsers.contains(it)) {
                                selectedUsers.remove(it)
                            } else {
                                selectedUsers[it] = true
                            }
                        }) {
                            Checkbox(
                                checked = selectedUsers.contains(it),
                                onCheckedChange = { _ ->
                                    if (selectedUsers.contains(it)) {
                                        selectedUsers.remove(it)
                                    } else {
                                        selectedUsers[it] = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
            if (state.loading) {
                CircularProgressIndicator()
            }
            ErrorText(state.error)

            Row(
                modifier = Modifier.align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                AnimatedVisibility(
                    visible = scrolled,
                ) {
                    LoadingButton("Back to top", onClick = {
                        scope.launch {
                            scrollState.animateScrollToItem(0)
                        }
                    })
                }

                AnimatedVisibility(visible = selectedUsers.isNotEmpty()) {
                    LoadingButton("Download Selected ${selectedUsers.size}", onClick = {
                        viewModel.downloadImages(false, selectedUsers.toMap().keys.toList())
                        selectedUsers.clear()
                    })
                }
            }

        }
    }
    if (showDownloadDialog) {
        Dialog(
            onDismissRequest = { showDownloadDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier.padding(horizontal = 16.dp)
                    .widthIn(max = 600.dp)
            ) {
                Box(Modifier) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                    ) {
                        Text("Download Images")
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DateInputField(
                                modifier = Modifier.weight(1f),
                                label = "Start Date",
                                placeholder = "Select start Date",
                                inputState = startDate
                            )
                            DateInputField(
                                modifier = Modifier.weight(1f),
                                label = "End Date",
                                placeholder = "Select end Date",
                                inputState = endDate
                            )

                        }
                        LoadingButton(
                            enabled = remember(startDate.isValid, endDate.isValid) {
                                derivedStateOf {
                                    startDate.isValid && endDate.isValid
                                }
                            }.value,
                            text = "Download",
                            onClick = {
                                viewModel.downloadImages(false)
                            },
                            fillWidth = true,
                            type = LoadingButtonType.DEFAULT
                        )
                        Spacer(Modifier.height(8.dp))
                        LoadingButton(
                            text = "Download All ${state.totalImages}",
                            onClick = {
                                viewModel.downloadImages(all = true)
                                showDownloadDialog = false
                            },
                            fillWidth = true,
                            type = LoadingButtonType.TEXT
                        )
                    }

                    IconButton(
                        onClick = {
                            showDownloadDialog = false
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Default.Close, "close")
                    }
                }
            }
        }
    }
    ProgressDialog(
        value = viewModel.imageDownloadState.collectAsStateWithLifecycle().value,
        onCancel = {
            viewModel.cancelDownload()
        }

    )

}

@Composable
fun ProgressDialog(
    onCancel: () -> Unit,
    value: ImageDownloadingState
) {

    if (value.progress >= 0) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Card(
                modifier = Modifier
                    .widthIn(600.dp)
                    .padding(horizontal = 32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                ) {
                    Text("Image Downloading")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier.weight(1f)
                                .height(10.dp),
                            progress = { value.progress.toFloat() / 100f },
                            strokeCap = StrokeCap.Round,
                            trackColor = MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.8f)
                        )
                        LoadingButton(
                            modifier = Modifier.width(120.dp),
                            text = "Cancel",
                            onClick = {
                                onCancel()
                            },
                            loading = value.cancelling
                        )
                    }
                }
            }
        }
    }


}
