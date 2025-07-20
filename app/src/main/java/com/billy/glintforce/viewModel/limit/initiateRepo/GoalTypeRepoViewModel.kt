package com.billy.glintforce.viewModel.limit.initiateRepo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billy.glintforce.data.toRemove.goalTypeDatabase.GoalType
import com.billy.glintforce.data.goalTypeTypeDatabase.GoalTypeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Class containing updated list of GoalTypes
 */
class GoalTypeRepoViewModel(goalTypeRepository: GoalTypeRepository) : ViewModel() {
    val goalTypeRepoUiState: StateFlow<GoalTypeRepoUiState> =
        goalTypeRepository.getAllGoalTypesStream().map { GoalTypeRepoUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = GoalTypeRepoUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}


/**
 * Filler class to insert list of GoalTypes
 */
data class GoalTypeRepoUiState(val goalTypeList: List<GoalType> = listOf())