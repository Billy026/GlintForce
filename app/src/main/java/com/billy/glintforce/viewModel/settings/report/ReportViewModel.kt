package com.billy.glintforce.viewModel.settings.report

import androidx.lifecycle.ViewModel
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Class containing the ReportUiState and required functions to update and utilise it
 */
class ReportViewModel : ViewModel() {
    // Get most recent ReportUiState
    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    // Toggles expansion state of selected card
    fun toggleExpansion(years: String) {
        when (years) {
            "previous" -> _uiState.update { currentState ->
                currentState.copy(previousExpanded = !currentState.previousExpanded)
            }

            "current" -> _uiState.update { currentState ->
                currentState.copy(currentExpanded = !currentState.currentExpanded)
            }

            else -> {}
        }
    }

    // Initiates ExpenseList on start up
    fun updateExpenseList(list: List<Expense>) {
        _uiState.update { currentState ->
            currentState.copy(expenseList = list)
        }
    }
}