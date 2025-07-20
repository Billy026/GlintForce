package com.billy.glintforce.viewModel.limit.initiateRepo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billy.glintforce.data.toRemove.goalDatabase.Goal
import com.billy.glintforce.data.toRemove.goalDatabase.GoalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Class containing updated list of Goals
 */
class GoalRepoViewModel(
    private val goalRepository: GoalRepository,
    private val userId: String
) : ViewModel() {
    val goalUiState: StateFlow<GoalUiState> =
        goalRepository.getAllGoalsStream(userId).map { GoalUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = GoalUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Filler class to insert list of Goals
 */
data class GoalUiState(val goalList: List<Goal> = listOf())