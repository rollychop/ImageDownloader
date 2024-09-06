package com.invictus.img.downloader.ui.screen.home.users

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.invictus.img.downloader.di.Di
import com.invictus.img.downloader.domain.model.user.IdCardUserModel
import com.invictus.img.downloader.ui.component.textfield.DataTextFieldState
import com.invictus.img.downloader.util.AppFileHelper
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.util.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

data class ImageDownloadingState(
    val progress: Int = -1,
    val totalImage: Int = 0,
    val cancelling: Boolean = false,
    val downloaded: Int = 0,
)


data class UsersScreenState(
    val loading: Boolean = false,
    val error: String = "",
    val users: List<IdCardUserModel> = emptyList(),
    val filteredUsers: List<IdCardUserModel> = emptyList(),
    val totalImages: Int = 0,
)

class UsersViewModel : ViewModel() {

    companion object {
        private val start = DataTextFieldState<Long>(
            validator = { data, _ ->
                true
            },
            errorFor = { data, _ ->
                "Select a valid start date"
            }
        )
        private val end = DataTextFieldState<Long>(
            validator = { data, _ ->
                true
            },
            errorFor = { data, _ ->
                "Select a valid end date"
            }
        )
    }

    private val repository = Di.idCarRepository

    private val _state = MutableStateFlow(UsersScreenState())
    val state: StateFlow<UsersScreenState> = _state.asStateFlow()
    private var prefix: String? = null


    private val _imageDownloadState = MutableStateFlow(ImageDownloadingState())
    val imageDownloadState: StateFlow<ImageDownloadingState> = _imageDownloadState.asStateFlow()

    var filterQuery by mutableStateOf("")
        private set


    private var filterJob: Job? = null
    fun filter(query: String) {
        filterQuery = query
        filterJob?.cancel()
        filterJob = viewModelScope.launch {
            delay(500)
            _state.update { s ->
                s.copy(
                    filteredUsers = getFilteredUsers(query, s.users)
                )
            }

        }
    }

    val startAndEndTextFieldState = start to end
    fun load(prefix: String) {
        this.prefix = prefix
        _state.update { UsersScreenState(loading = true) }
        viewModelScope.launch {
            repository.getUsers(prefix).fold(
                onSuccess = {
                    _state.update { _ ->
                        UsersScreenState(
                            users = it,
                            filteredUsers = getFilteredUsers(filterQuery, it),
                            totalImages = it.count { it.picture.startsWith("https://") })
                    }

                },
                onFailure = {
                    _state.update { _ -> UsersScreenState(error = "${it.message}") }

                }
            )
        }


    }

    private var httpClient: io.ktor.client.HttpClient? = null
    private var downloadingJob: Job? = null

    private fun getFilteredUsers(
        query: String,
        users: List<IdCardUserModel>
    ) = if (query.isBlank()) users else users.filter {
        it.fullName.contains(query, ignoreCase = true) ||
                it.enrollmentNo.contains(query, ignoreCase = true) ||
                it.rollNo.contains(query, ignoreCase = true) ||
                it.classId.contains(query, ignoreCase = true) ||
                it.section.contains(query, ignoreCase = true) ||
                it.loginId.contains(query, ignoreCase = true)
    }


    @OptIn(InternalAPI::class)
    fun downloadImages(all: Boolean, list: List<IdCardUserModel>? = null) {
        _imageDownloadState.update { ImageDownloadingState(progress = 0) }

        if (httpClient == null) {
            httpClient = io.ktor.client.HttpClient(OkHttp)
        }

        downloadingJob?.cancel()
        downloadingJob = viewModelScope.launch {
            val pictures = (list ?: if (all) {
                state.value.users
            } else {
                state.value.users.filter {
                    val start = startAndEndTextFieldState.first.data
                    val end = startAndEndTextFieldState.second.data
                    if (start != null && end != null && it.lastClickedDate != null) {
                        val range = start..end
                        it.lastClickedDate in range
                    } else false
                }
            })
                .filter { it.picture.startsWith("https://") }

            if (pictures.isEmpty()) {
                _imageDownloadState.update { ImageDownloadingState() }
                return@launch
            }

            _imageDownloadState.update { s -> s.copy(totalImage = pictures.size) }

            runCatching {
                val file = File(
                    File(AppFileHelper.getUserHomeDirectory(), prefix ?: "qt"),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
                ).apply {
                    mkdirs()
                }

                pictures.map { u ->
                    async(Dispatchers.IO) {
                        val response = httpClient?.get(u.picture) ?: throw IOException("HttpClient is null")
                        val imageFile = File(file, "${u.applicationNo}.jpeg")
                        response.content.copyAndClose(imageFile.writeChannel())
                        _imageDownloadState.update { state ->
                            state.copy(
                                progress = (state.downloaded + 1)
                                    .toDouble()
                                    .div(pictures.size)
                                    .times(100)
                                    .roundToInt(),
                                downloaded = state.downloaded + 1
                            )
                        }
                    }
                }.awaitAll()
            }

            _imageDownloadState.update { s -> s.copy(progress = -1) }
        }
    }

    fun cancelDownload() {
        _imageDownloadState.update { s -> s.copy(cancelling = true) }
        viewModelScope.launch(Dispatchers.IO) {
            httpClient?.close()
            httpClient = null
            downloadingJob?.cancelAndJoin()
            _imageDownloadState.update { _ -> ImageDownloadingState() }
        }
    }


    override fun onCleared() {
        httpClient?.close()
        downloadingJob?.cancel()
        httpClient = null
        super.onCleared()
    }
}
