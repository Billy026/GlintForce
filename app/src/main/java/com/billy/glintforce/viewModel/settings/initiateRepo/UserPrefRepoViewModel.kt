package com.billy.glintforce.viewModel.settings.initiateRepo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billy.glintforce.data.toRemove.userPreferenceDatabase.UserPreference
import com.billy.glintforce.data.toRemove.userPreferenceDatabase.UserPreferenceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Class containing updated User Preferences
 */
class UserPrefRepoViewModel(userPrefRepository: UserPreferenceRepository) : ViewModel() {
    val userPrefRepoUiState: StateFlow<UserPrefRepoUiState> =
        userPrefRepository.getAllUserPrefsStream().map { UserPrefRepoUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = UserPrefRepoUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Filler class to insert User Preferences
 */
data class UserPrefRepoUiState(val userPrefList: List<UserPreference> = listOf())