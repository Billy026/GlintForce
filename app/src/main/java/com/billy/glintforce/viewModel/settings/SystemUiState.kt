package com.billy.glintforce.viewModel.settings

/**
 * Class containing all data needed for the SystemSettings page
 */
data class SystemUiState(
    // Lang drop-down menu
    val lang: String = "",
    val langList: List<String> = listOf(),
    val langIsExpanded: Boolean = false,
    val langSelectedText: String = "English",

    // Report Rate switch
    val rateSwitch: Boolean = true,

    // Report Rate drop-down menu
    val rate: String = "",
    val rateList: List<String> = listOf(),
    val rateIsExpanded: Boolean = false,
    val rateSelectedText: String = "",

    // Change Theme switch
    val themeSwitch: Boolean = false,

    // Required lists
    val categoryList: List<String> = listOf(""),
    val labelList: List<String> = listOf(),
    val goalTypeList: List<String> = listOf()
)
