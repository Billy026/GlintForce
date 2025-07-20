package com.billy.glintforce.viewModel.database

import androidx.lifecycle.ViewModel
import com.billy.glintforce.data.toRemove.categoryDatabase.Category
import com.billy.glintforce.data.toRemove.categoryDatabase.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Class containing the CategoryUiState and required functions to update and utilise it
 */
class CategoryViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    // Most recent CategoryUiState
    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    // Updates the desc data
    fun updateDesc(desc: String) {
        _uiState.update { currentState ->
            currentState.copy(desc = desc)
        }
    }

    // Resets text fields
    fun resetTextFields() {
        _uiState.update { currentState ->
            currentState.copy(desc = "")
        }
    }

    // Adds the inputted data into categoryList as a Category
    suspend fun addCategory() {
        categoryRepository.insertCategory(_uiState.value.toCategory())
    }

    // Deletes Category from categoryList
    suspend fun deleteCategory(category: Category) {
        categoryRepository.deleteCategory(category = category)
    }

    // Updates Category from categoryList to a new Category
    suspend fun updateCategory(category: Category, desc: String) {
        categoryRepository.updateCategory(id = category.id, desc = desc)
    }
}