package com.billy.glintforce.viewModel.limit.mainLimitPage

import com.billy.glintforce.data.toRemove.goalDatabase.Goal

/**
 * Class containing all data needed for the Limit page
 */
data class LimitUiState(
    val userId: String = "",

    // Goal/Reminder TextField
    val goalOrReminder: String = "",
    val goalOrReminderList: List<String> = listOf(),
    val goalOrReminderIsExpanded: Boolean = false,
    val GRSelectedText: String = "",

    // Type TextField
    val type: String = "",
    val goalTypeList: List<String> = listOf(),
    val reminderTypeList: List<String> = listOf(),
    val typeIsExpanded: Boolean = false,
    val typeSelectedText: String = "",

    // Description TextField
    val desc: String = "",

    // Amount TextField
    val amount: String = "",
    val amountError: String? = null,
)

// Converts data in the UiState into a Goal instance
fun LimitUiState.toGoal(userId: String, date: String, time: String): Goal = Goal(
    id = 0,
    userId = userId,
    gORr = goalOrReminder,
    type = type,
    desc = desc,
    amount = amount.toDouble(),
    startDate = date,
    startTime = time
)