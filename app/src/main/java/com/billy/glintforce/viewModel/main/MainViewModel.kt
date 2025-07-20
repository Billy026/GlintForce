package com.billy.glintforce.viewModel.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Class containing the MainUiState and required functions to update and utilise it
 */
class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun toggleLaunch() {
        _uiState.update { currentState ->
            currentState.copy(launch = !currentState.launch)
        }
    }
}