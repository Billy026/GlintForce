package com.billy.glintforce.mainApplication.settingsTab.settingsOptions

import com.billy.glintforce.R

/**
 * List of all Settings Options
 */
object SettingsOptionsDatasource {
    val settingsOptionsList = listOf(
        SettingsOptions(title = R.string.report, screen = "report"),
        SettingsOptions(title = R.string.theme, screen = "system"),
        SettingsOptions(title = R.string.system, screen = "system")
    )
}