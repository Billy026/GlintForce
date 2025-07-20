package com.billy.glintforce.viewModel.database

import androidx.lifecycle.ViewModel
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.categoryDatabase.CategoryRepository
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.data.toRemove.expenseDatabase.ExpenseRepository
import com.billy.glintforce.mainApplication.UiText
import com.billy.glintforce.common.getValidated
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Class containing the DatabaseUiState and required functions to update and utilise it
 */
class DatabaseViewModel(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    // Most recent DatabaseUiState
    private val _uiState = MutableStateFlow(DatabaseUiState())
    val uiState: StateFlow<DatabaseUiState> = _uiState.asStateFlow()

    // Translate all function lists
    init {
        if (uiState.value.sortList.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    sortList = listOf(
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.byDate), 0),
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.byCategory), 1),
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.byCost), 2)
                    ),
                    filterList = listOf(
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.byDate), 1),
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.byCategory), 2),
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.byCostRange), 3)
                    ),
                    calculateList = listOf(
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.totalExp), 0),
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.aveExp), 1),
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.calcByDate), 2),
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.calcByCategory), 3)
                    ),
                    modelList = listOf(
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.barChart), 0),
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.lineChart), 1),
                        Pair(com.billy.glintforce.mainApplication.UiText.StringResource(resId = R.string.pieChart), 2)
                    )
                )
            }
        }
    }

    // Sets available Categories to all Category entities in CategoryRepository
    fun setCategories(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    categoryList = categoryRepository
                        .getAllCategoriesStream()
                        .first()
                        .map { it.desc }
                )
            }
        }
    }

    // Toggles the sortAscending state
    fun toggleAscending() {
        _uiState.update { currentState ->
            currentState.copy(ascending = !currentState.ascending)
        }
    }

    // Toggles the expansion of the Category drop-down menu
    fun toggleExpansion() {
        _uiState.update { currentState ->
            currentState.copy(categoryExpanded = !currentState.categoryExpanded)
        }
    }

    // Sets the display of the pop-up dialog
    fun setDialog(bool: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(dialogShown = bool)
        }
    }

    // Sets which pop-up dialog should be shown
    fun setDialogId(string: String) {
        _uiState.update { currentState ->
            currentState.copy(dialogId = string)
        }
    }

    // Saves user input from datePicker dialog
    fun saveDate(date: String, filter: Boolean) {
        if (filter) {
            _uiState.update { currentState ->
                currentState.copy(date = date)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(byDate = date)
            }
        }
    }

    // Saves user input for cost range pop-up dialog
    fun updateText(string: String, int: String) {
        when (string) {
            "minCost" -> _uiState.update { currentState ->
                currentState.copy(minCost = getValidated(int))
            }

            "maxCost" -> _uiState.update { currentState ->
                currentState.copy(maxCost = getValidated(int))
            }
        }
    }

    // Saves selected user input from Category pop-up dialog
    fun updateSelected(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(categorySelected = currentState.categoryList[index])
        }
    }

    // Reflects selected Category in drop-down menu
    fun updateDropDown() {
        _uiState.update { currentState ->
            currentState.copy(category = currentState.categorySelected)
        }
    }

    // Updates sort state of Expenses in table
    fun updateSort(int: Int) {
        _uiState.update { currentState ->
            currentState.copy(currentSort = int)
        }
    }

    // Updates filter state of Expenses in table
    fun updateFilter(int: Int) {
        _uiState.update { currentState ->
            currentState.copy(currentFilter = int)
        }
    }

    // Reset all text fields to their original state
    fun resetText() {
        _uiState.update { currentState ->
            currentState.copy(
                category = "",
                categorySelected = "",
                minCost = "",
                maxCost = "",
                byDate = ""
            )
        }
    }

    // Reset all selected user input to their original state
    fun resetSelected() {
        _uiState.update { currentState ->
            currentState.copy(
                categorySelected = "",
            )
        }
    }

    // Sets expansion for buttons
    fun setExpansion(buttonIndex: Int, bool: Boolean) {
        _uiState.update { currentState ->
            when (buttonIndex) {
                0 -> currentState.copy(sortExpanded = bool)
                1 -> currentState.copy(filterExpanded = bool)
                2 -> currentState.copy(calculateExpanded = bool)
                3 -> currentState.copy(modelExpanded = bool)
                4 -> currentState.copy(barChartExpanded = bool)
                else -> currentState.copy(lineChartExpanded = bool)
            }
        }
    }

    // Deletes selected Expense from ExpenseRepository
    suspend fun deleteExpense(expense: Expense) {
        expenseRepository.deleteExpense(expense = expense)
    }
}