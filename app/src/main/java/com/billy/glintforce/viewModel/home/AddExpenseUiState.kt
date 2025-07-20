package com.billy.glintforce.viewModel.home

import com.billy.glintforce.data.toRemove.expenseDatabase.Expense

/**
 * Class containing all data needed for the Add Expense page
 */
data class AddExpenseUiState(
    // Category text field
    val category: String = "",
    val categoryList: List<String> = listOf(),
    val categoryIsExpanded: Boolean = false,
    val categorySelectedText: String = "",

    val desc: String = "",

    val cost: String = ""
)

// Converts data in the UiState into an Expense instance
fun AddExpenseUiState.toExpense(
    userId: String,
    date: String,
    time: String,
    month: String
): Expense = Expense(
    id = 0,
    userId = userId,
    category = category,
    desc = desc,
    cost = cost.toDouble(),
    date = date,
    time = time,
    month = month
)
