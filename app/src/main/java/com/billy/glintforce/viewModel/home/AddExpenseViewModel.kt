package com.billy.glintforce.viewModel.home

import androidx.lifecycle.ViewModel
import com.billy.glintforce.data.toRemove.categoryDatabase.CategoryRepository
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.data.toRemove.expenseDatabase.ExpenseRepository
import com.billy.glintforce.common.getValidated
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Class containing the AddExpenseUiState and required functions to update and utilise it
 */
class AddExpenseViewModel(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    private val userId: String
) : ViewModel() {
    // Most recent AddExpenseUiState
    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()

    // Toggles expansion state of Category drop-down menu
    fun toggleExpansion() {
        _uiState.update { currentState ->
            currentState.copy(categoryIsExpanded = !currentState.categoryIsExpanded)
        }
    }

    // Sets expansion state of Category drop-down menu
    fun setExpansion(bool: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(categoryIsExpanded = bool)
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

    // Saves selected user input from Category drop-down menu
    fun updateSelected(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                categorySelectedText = currentState.categoryList[index]
            )
        }
    }

    // Reflects selected Category in drop-down menu
    fun updateDropDown() {
        _uiState.update { currentState ->
            currentState.copy(
                category = currentState.categorySelectedText
            )
        }
    }

    // Updates desc property
    fun updateDesc(desc: String) {
        _uiState.update { currentState ->
            currentState.copy(desc = desc)
        }
    }

    // Updates cost property
    fun updateCost(cost: String) {
        _uiState.update { currentState ->
            currentState.copy(cost = getValidated(cost))
        }
    }

    // Reset all text fields to their original state
    fun resetTextFields(categorySelectedText: String) {
        _uiState.update { currentState ->
            currentState.copy(
                category = "",
                categorySelectedText = categorySelectedText,
                desc = "",
                cost = ""
            )
        }
    }

    // Getter function for userId
    fun getUserId(): String {
        return userId
    }

    // Inserts a new Expense into ExpenseRepository
    suspend fun addExpense(date: String, time: String, month: String) {
        expenseRepository.insertExpense(
            _uiState.value.toExpense(
                userId = userId,
                date = date,
                time = time,
                month = month
            )
        )
    }

    // Edits an existing Expense in ExpenseRepository
    suspend fun editExpense(expense: Expense, category: String, desc: String, cost: String) {
        expenseRepository.updateExpense(
            id = expense.id,
            userId = userId,
            category = category,
            desc = desc,
            cost = cost.toDouble()
        )
    }
}