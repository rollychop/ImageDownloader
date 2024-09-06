package com.invictus.img.downloader.ui.screen.home.dashboard

import com.invictus.img.downloader.di.Di
import com.invictus.img.downloader.domain.model.OrganisationModel
import com.invictus.img.downloader.ui.component.textfield.DataTextFieldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope


data class DashboardScreenState(
    val loading: Boolean = false,
    val error: String = "",
    val organisations: List<OrganisationModel> = emptyList(),
)

class DashboardViewModel : ViewModel() {

    private val repository = Di.idCarRepository

    private val _state = MutableStateFlow(DashboardScreenState())
    val state: StateFlow<DashboardScreenState> = _state.asStateFlow()



    init {
        loadOrganisations()
    }


    private fun loadOrganisations() {
        _state.update { DashboardScreenState(loading = true) }
        viewModelScope.launch {
            repository.getOrganisation(null)
                .fold(
                    onSuccess = {
                        _state.update { s ->
                            s.copy(loading = false, organisations = it)
                        }
                    },
                    onFailure = {
                        _state.update { s -> s.copy(loading = false, error = "${it.message}") }
                    },
                )
        }
    }

}
