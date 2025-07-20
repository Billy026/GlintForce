package com.billy.glintforce.viewModel.limit.goalType

import androidx.lifecycle.ViewModel
import com.billy.glintforce.data.toRemove.goalTypeDatabase.GoalType
import com.billy.glintforce.data.goalTypeTypeDatabase.GoalTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Class containing the GoalTypeUiState and required functions to update and utilise it
 */
class GoalTypeViewModel(private val goalTypeRepository: GoalTypeRepository) : ViewModel() {
    // Most recent GoalTypeUiState
    private val _uiState = MutableStateFlow(GoalTypeUiState())
    val uiState: StateFlow<GoalTypeUiState> = _uiState.asStateFlow()

    // Initialises gORrList on start up
    fun initialiseList(list: List<String>) {
        _uiState.update { currentState ->
            currentState.copy(gORrList = list)
        }
    }

    // Toggles the expanded state of gORr drop-down menu
    fun toggleExpansion() {
        _uiState.update { currentState ->
            currentState.copy(gORrIsExpanded = !currentState.gORrIsExpanded)
        }
    }

    // Sets the expanded state of gORr drop-down menu
    fun setExpansion(boolean: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(gORrIsExpanded = boolean)
        }
    }

    // Resets text fields to their original states
    fun resetTextFields(label: String) {
        _uiState.update { currentState ->
            currentState.copy(
                gORr = "",
                gORrSelectedText = label,
                type = ""
            )
        }
    }

    // Updates the type property
    fun updateType(string: String) {
        _uiState.update { currentState ->
            currentState.copy(type = string)
        }
    }

    // Toggles the max? property
    fun updateSwitch() {
        _uiState.update { currentState ->
            currentState.copy(max = !currentState.max)
        }
    }

    // Retrieves the text for each option in the drop down text field
    fun updateSelected(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(gORrSelectedText = currentState.gORrList[index])
        }
    }

    // Updates the gORr to the selected drop-down value
    fun updateDropDown() {
        _uiState.update { currentState ->
            currentState.copy(gORr = currentState.gORrSelectedText)
        }
    }

    // Inserts new GoalType into GoalTypeRepository
    suspend fun addGoalType(goals: String) {
        goalTypeRepository.insertGoalType(_uiState.value.toGoalType(goals = goals))
    }

    // Deletes selected GoalType from GoalTypeRepository
    suspend fun deleteGoalType(goalType: GoalType) {
        goalTypeRepository.deleteGoalType(goalType)
    }

    // Edits selected GoalType in GoalTypeRepository
    suspend fun updateGoalType(goalType: GoalType, gORr: String, type: String, max: Boolean?) {
        goalTypeRepository.updateGoalType(id = goalType.id, gORr = gORr, type = type, max = max)
    }
}