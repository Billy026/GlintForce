package com.billy.glintforce.mainApplication.settingsTab.settingsOptions

import androidx.annotation.StringRes

/**
 * Data class to store all details needed for an option in Settings page
 */
data class SettingsOptions(
    @StringRes val title: Int,
    val screen: String
)
