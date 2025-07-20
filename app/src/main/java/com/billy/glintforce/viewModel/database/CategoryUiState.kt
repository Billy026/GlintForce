package com.billy.glintforce.viewModel.database

import com.billy.glintforce.data.toRemove.categoryDatabase.Category

/**
 * Class containing all data needed for Category Screens
 */
data class CategoryUiState(
    val desc: String = ""
)

// Converts data in the UiState into a Category instance
fun CategoryUiState.toCategory(): Category = Category(
    id = 0,
    desc = desc,
    editable = true
)


