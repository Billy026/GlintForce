package com.billy.glintforce.viewModel.settings.initiateRepo

data class UserPreferences(
    val id: Int = 0,
    val userId: String = "",
    val showWidget: Boolean = true,
    val lang: String = "",
    val report: Int = 0,
    val dateGenerated: String = "",
    val reportGenerated: Boolean = false,
    val theme: Boolean = false
)
