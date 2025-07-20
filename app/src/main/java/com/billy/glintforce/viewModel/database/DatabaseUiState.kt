package com.billy.glintforce.viewModel.database

import com.billy.glintforce.mainApplication.UiText

/**
 * Class containing all data needed for the Database page
 */
data class DatabaseUiState(
    val ascending: Boolean = true,
    val dialogShown: Boolean = false,
    val dialogId: String = "",

    // Expanded values
    val sortExpanded: Boolean = false,
    val filterExpanded: Boolean = false,
    val calculateExpanded: Boolean = false,
    val modelExpanded: Boolean = false,
    val barChartExpanded: Boolean = false,
    val lineChartExpanded: Boolean = false,

    // Lists for the buttons
    val sortList: List<Pair<com.billy.glintforce.mainApplication.UiText.StringResource, Int>> = listOf(),
    val filterList: List<Pair<com.billy.glintforce.mainApplication.UiText.StringResource, Int>> = listOf(),
    val calculateList: List<Pair<com.billy.glintforce.mainApplication.UiText.StringResource, Int>> = listOf(),
    val modelList: List<Pair<com.billy.glintforce.mainApplication.UiText.StringResource, Int>> = listOf(),

    // Current values for buttons
    val currentSort: Int = 0,
    val currentFilter: Int = 0,

    // Values for pop-up dialogs
    val date: String = "",
    val byDate: String = "",
    val minCost: String = "",
    val maxCost: String = "",

    // Values for Category pop-up dialog
    val category: String = "",
    val categoryExpanded: Boolean = false,
    val categorySelected: String = "",
    val categoryList: List<String> = listOf(),
)
