package com.billy.glintforce.viewModel.screenTemplate

import com.billy.glintforce.data.toRemove.expenseDatabase.Expense

/**
 * Class containing all the data needed for AppScreen
 */
data class GlintForceUiState(
    // For tab operations
    val selectedTabIndex: Int = 0,
    val isSettings: Boolean = false,

    // For widget
    val expenseList: List<Expense> = listOf(),

    // For pop-up dialog
    val showDialog: Boolean = false,
    val checked: Boolean = false,
    val firstClicked: Boolean = true
)