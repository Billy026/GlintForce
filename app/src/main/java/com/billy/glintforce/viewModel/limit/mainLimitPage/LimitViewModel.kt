package com.billy.glintforce.viewModel.limit.mainLimitPage

import androidx.lifecycle.ViewModel
import com.billy.glintforce.data.toRemove.goalDatabase.Goal
import com.billy.glintforce.data.toRemove.goalDatabase.GoalRepository
import com.billy.glintforce.data.goalTypeTypeDatabase.GoalTypeRepository
import com.billy.glintforce.common.getValidated
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Class containing the LimitUiState and required functions to update and utilise it
 */
class LimitViewModel(
    private val goalRepository: GoalRepository,
    private val goalTypeRepository: GoalTypeRepository,
    private val userId: String
) : ViewModel() {
    // Most recent LimitUiState
    private val _uiState = MutableStateFlow(LimitUiState())
    val uiState: StateFlow<LimitUiState> = _uiState.asStateFlow()

    // Initialises goalOrReminderList on start up
    fun initialiseGoalReminderList(list: List<String>) {
        _uiState.update { currentState ->
            currentState.copy(goalOrReminderList = list)
        }
    }

    // Checks if the Goal/Reminder data is blank
    fun checkCurrentEmpty(): Boolean {
        return uiState.value.goalOrReminder == ""
    }

    // Sets available GoalTypes to all GoalType entities in GoalTypeRepository
    fun getGoalTypes(
        coroutineScope: CoroutineScope,
        string: String,
        goal: Boolean
    ) {
        coroutineScope.launch {
            _uiState.update { currentState ->
                if (goal) {
                    currentState.copy(
                        goalTypeList = goalTypeRepository
                            .getAllGoalTypesStream()
                            .first()
                            .filter { it.gORr == string }
                            .map { it.type }
                    )
                } else {
                    currentState.copy(
                        reminderTypeList = goalTypeRepository
                            .getAllGoalTypesStream()
                            .first()
                            .filter { it.gORr == string }
                            .map { it.type }
                    )
                }
            }
        }
    }

    // Toggles the respective expanded state
    fun toggleExpansion(string: String) {
        if (string == "type") {
            _uiState.update { currentState ->
                currentState.copy(typeIsExpanded = !currentState.typeIsExpanded)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(goalOrReminderIsExpanded = !currentState.goalOrReminderIsExpanded)
            }
        }
    }

    // Sets the respective expanded state
    fun setExpansion(string: String, boolean: Boolean) {
        if (string == "type") {
            _uiState.update { currentState ->
                currentState.copy(typeIsExpanded = boolean)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(goalOrReminderIsExpanded = boolean)
            }
        }
    }

    // Updates the respective data
    fun updateDropDown(type: String) {
        if (type == "goal") {
            _uiState.update { currentState ->
                currentState.copy(goalOrReminder = currentState.GRSelectedText)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(type = currentState.typeSelectedText)
            }
        }
    }

    // Updates the description property
    fun updateDesc(string: String) {
        _uiState.update { currentState ->
            currentState.copy(desc = string)
        }
    }

    // Updates the amount property
    fun updateAmount(int: String) {
        _uiState.update { currentState ->
            currentState.copy(amount = getValidated(int))
        }
    }

    // Retrieves the respective text for each option in the drop down text field
    fun updateSelected(index: Int, type: Boolean, goalStr: String) {
        if (type) {
            _uiState.update { currentState ->
                currentState.copy(
                    typeSelectedText = if (currentState.goalOrReminder == goalStr) {
                        currentState.goalTypeList[index]
                    } else {
                        currentState.reminderTypeList[index]
                    }
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(GRSelectedText = currentState.goalOrReminderList[index])
            }
        }
    }

    // Resets all text fields to their original state
    fun resetDropDown(label: String, type: String) {
        _uiState.update { currentState ->
            currentState.copy(
                goalOrReminder = "",
                GRSelectedText = label,
                type = "",
                typeSelectedText = type
            )
        }
    }

    // Resets the selected text of the Type drop down text field
    fun resetType(type: String) {
        _uiState.update { currentState ->
            currentState.copy(typeSelectedText = type, type = "")
        }
    }

    // Inserts new Goal into GoalRepository
    suspend fun addToCurrent(date: String, time: String) {
        goalRepository.insertGoal(_uiState.value.toGoal(userId = userId, date = date, time = time))
    }

    // Deletes selected Goal from GoalRepository
    suspend fun delete(goal: Goal) {
        goalRepository.deleteGoal(goal)
    }

    // Updates Goal from goalList to a new Goal
    suspend fun updateGoal(goal: Goal, gORr: String, type: String, desc: String, amount: String) {
        goalRepository.updateGoal(
            id = goal.id,
            userId = userId,
            gORr = gORr,
            type = type,
            desc = desc,
            amount = amount.toDouble()
        )
    }
}