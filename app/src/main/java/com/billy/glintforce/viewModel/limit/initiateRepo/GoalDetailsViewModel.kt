package com.billy.glintforce.viewModel.limit.initiateRepo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billy.glintforce.data.toRemove.goalDatabase.Goal
import com.billy.glintforce.data.toRemove.goalDatabase.GoalRepository
import com.billy.glintforce.mainApplication.limitTab.limitScreen.goalInputScreen.GoalDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Class to get current selected Goal for navigation
 */
class GoalDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val goalRepository: GoalRepository,
    private val userId: String
) : ViewModel() {
    private val goalId: Int
            = checkNotNull(savedStateHandle[GoalDetailsDestination.GOALIDARG])

    val uiState: StateFlow<GoalDetailsUiState> =
        goalRepository.getGoalStream(goalId, userId)
            .filterNotNull()
            .map { GoalDetailsUiState(goal = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = GoalDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Filler class to insert selected Goal
 */
data class GoalDetailsUiState(
    val goal: Goal = Goal(userId = "", gORr = "", type = "", amount = 0.0, startDate = "", startTime = "")
)
