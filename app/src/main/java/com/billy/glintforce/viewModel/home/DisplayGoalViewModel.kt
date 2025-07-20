package com.billy.glintforce.viewModel.home

import androidx.lifecycle.ViewModel
import com.billy.glintforce.data.toRemove.goalDatabase.Goal
import com.billy.glintforce.data.toRemove.goalDatabase.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Class containing the DisplayGoalUiState and required functions to update and utilise it
 */
class DisplayGoalViewModel(
    private val goalRepository: GoalRepository,
    private val userId: String
) : ViewModel() {
    // Most recent DisplayGoalUiState
    private val _uiState = MutableStateFlow(DisplayGoalUiState())
    val uiState: StateFlow<DisplayGoalUiState> = _uiState.asStateFlow()

    // Updates all Goals and Reminders to the state of the selectAll button
    fun updateAll(bool: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(selectAll = bool)
        }
    }

    // Updates the display of the selectAll button
    suspend fun selectAllChange(list: List<Goal>) {
        if (uiState.value.selectAll) {
            list.forEach {
                updateDisplay(goal = it, bool = true)
            }
        } else {
            list.forEach {
                updateDisplay(goal = it, bool = false)
            }
        }
    }

    // Updates the display property of the selected Goal or Reminder
    suspend fun updateDisplay(goal: Goal, bool: Boolean) {
        goalRepository.updateDisplay(id = goal.id, userId = userId, display = bool)
    }
}