package com.billy.glintforce.viewModel.limit.goalType

import com.billy.glintforce.data.toRemove.goalTypeDatabase.GoalType

/**
 * Class containing all data needed for the Insert Type Tab
 */
data class GoalTypeUiState(
    // gORr text field
    val gORr: String = "",
    val gORrList: List<String> = listOf(),
    val gORrIsExpanded: Boolean = false,
    val gORrSelectedText: String = "",

    val type: String = "",

    val max: Boolean = false
)

// Converts data in the UiState into a GoalType instance
fun GoalTypeUiState.toGoalType(goals: String): GoalType = GoalType(
    id = 0,
    gORr = gORr,
    type = type,
    max = if (gORr == goals) {
        max
    } else {
        null
    },
    editable = true
)