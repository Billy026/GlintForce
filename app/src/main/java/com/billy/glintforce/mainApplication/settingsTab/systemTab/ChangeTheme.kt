package com.billy.glintforce.mainApplication.settingsTab.systemTab

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import com.billy.glintforce.R
import com.billy.glintforce.mainApplication.settingsTab.SettingsRow
import com.billy.glintforce.viewModel.settings.SystemUiState

/**
 * Switch to change app theme
 */
@Composable
fun ChangeTheme(
    // Actions
    onCheckedChange: (Boolean) -> Unit,

    // UiStates
    uiState: SystemUiState
) {
    SettingsRow(
        title = R.string.darkTheme,
        switchUi = {
            Switch(
                checked = uiState.themeSwitch,
                onCheckedChange = onCheckedChange
            )
        }
    )
}
