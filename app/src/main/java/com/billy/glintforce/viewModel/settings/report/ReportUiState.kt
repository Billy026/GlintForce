package com.billy.glintforce.viewModel.settings.report

import com.billy.glintforce.data.toRemove.expenseDatabase.Expense

/**
 * Class containing all data needed for the Report page
 */
data class ReportUiState(
    val monthList: List<String> = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    ),

    val previousExpanded: Boolean = false,
    val currentExpanded: Boolean = true,

    val expenseList: List<Expense> = listOf()
)