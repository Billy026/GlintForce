package com.billy.glintforce.viewModel.screenTemplate

import androidx.lifecycle.ViewModel
import com.billy.glintforce.data.toRemove.userPreferenceDatabase.UserPreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Class containing the GlintForceUiState and required functions to update and utilise it
 */
class GlintForceViewModel(
    private val userPrefRepo: UserPreferenceRepository
) : ViewModel() {
    // Most recent GlintForceUiState
    private val _uiState = MutableStateFlow(GlintForceUiState())
    val uiState: StateFlow<GlintForceUiState> = _uiState.asStateFlow()

    // Toggles checkbox
    fun toggleCheckbox() {
        _uiState.update { currentState ->
            currentState.copy(checked = !currentState.checked)
        }
    }

    // Sets firstClick property to false
    fun setFirstClick() {
        _uiState.update { currentState ->
            currentState.copy(firstClicked = false)
        }
    }

    // Updates the current selected tab
    fun updateTab(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(selectedTabIndex = index)
        }
    }

    // Updates whether the Settings Tab is selected
    fun updateSettings(boolean: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isSettings = boolean)
        }
    }

    // Updates whether Pop Up is shown
    fun updateDialog(bool: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(showDialog = bool)
        }
    }

    // Updates showDialog property of User Preferences
    suspend fun toggleShowDialog(bool: Boolean) {
        userPrefRepo.updateWidget(id = 0, showWidget = bool)
    }

    // Updates reportGenerated property of User Preferences
    fun setGeneration(bool: Boolean, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            userPrefRepo.updateGeneration(id = 0, generated = bool)
        }
    }

    // Updates dateGenerated property of User Preferences
    fun updateDate(date: String, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            userPrefRepo.updateDate(id = 0, date = date)
        }
    }
}