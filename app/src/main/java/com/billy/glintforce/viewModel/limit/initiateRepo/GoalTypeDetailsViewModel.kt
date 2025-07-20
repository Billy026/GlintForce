package com.billy.glintforce.viewModel.limit.initiateRepo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billy.glintforce.data.toRemove.goalTypeDatabase.GoalType
import com.billy.glintforce.data.goalTypeTypeDatabase.GoalTypeRepository
import com.billy.glintforce.mainApplication.limitTab.goalType.GoalTypeDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Class to get current selected GoalType for navigation
 */
class GoalTypeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val goalTypeRepository: GoalTypeRepository
) : ViewModel() {
    private val goalTypeId: Int
        = checkNotNull(savedStateHandle[GoalTypeDetailsDestination.GOALTYPEIDARG])

    val uiState: StateFlow<GoalTypeDetailsUiState> =
        goalTypeRepository.getGoalTypeStream(goalTypeId)
            .filterNotNull()
            .map { GoalTypeDetailsUiState(goalType = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = GoalTypeDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Filler class to insert selected GoalType
 */
data class GoalTypeDetailsUiState(
    val goalType: GoalType = GoalType(gORr = "", type = "")
)