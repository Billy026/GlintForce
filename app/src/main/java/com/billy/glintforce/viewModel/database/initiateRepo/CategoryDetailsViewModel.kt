package com.billy.glintforce.viewModel.database.initiateRepo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billy.glintforce.data.toRemove.categoryDatabase.Category
import com.billy.glintforce.data.toRemove.categoryDatabase.CategoryRepository
import com.billy.glintforce.mainApplication.databaseTab.categories.CategoryDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Class to get current selected Category for navigation
 */
class CategoryDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val categoryId: Int
            = checkNotNull(savedStateHandle[CategoryDetailsDestination.CATEGORYIDARG])

    val uiState: StateFlow<CategoryDetailsUiState> =
        categoryRepository.getCategoryStream(categoryId)
            .filterNotNull()
            .map { CategoryDetailsUiState(category = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CategoryDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Filler class to insert selected Category
 */
data class CategoryDetailsUiState(
    val category: Category = Category(desc = "")
)