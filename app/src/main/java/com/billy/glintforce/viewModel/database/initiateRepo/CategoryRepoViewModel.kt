package com.billy.glintforce.viewModel.database.initiateRepo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billy.glintforce.data.toRemove.categoryDatabase.Category
import com.billy.glintforce.data.toRemove.categoryDatabase.CategoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Class containing updated list of Categories
 */
class CategoryRepoViewModel(categoryRepository: CategoryRepository) : ViewModel() {
    val categoryRepoUiState: StateFlow<CategoryRepoUiState> =
        categoryRepository.getAllCategoriesStream().map { CategoryRepoUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CategoryRepoUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Filler class to insert list of Categories
 */
data class CategoryRepoUiState(val categoryList: List<Category> = listOf())