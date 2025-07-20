package com.billy.glintforce.mainApplication.settingsTab.systemTab

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.billy.glintforce.R
import com.billy.glintforce.common.CreateDropDown
import com.billy.glintforce.mainApplication.settingsTab.SettingsRow
import com.billy.glintforce.viewModel.settings.SystemUiState

/**
 * Settings row used to set rate at which reports are automatically generated and downloaded
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportRate(
    onCheckedChange: (Boolean) -> Unit,
    onClick: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onExpandedChange: (Boolean) -> Unit,

    uiState: SystemUiState
) {
    SettingsRow(
        title = R.string.autoReport,
        switchUi = {
            Switch(
                checked = uiState.rateSwitch,
                onCheckedChange = onCheckedChange
            )
        }
    ) {
        if (uiState.rateSwitch) {
            CreateDropDown(
                expanded = uiState.rateIsExpanded,
                value = uiState.rateSelectedText,
                list = uiState.rateList,
                onExpandedChange = onExpandedChange,
                forEachIndexed = { index, text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = { onClick(index) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                },
                onDismissRequest = onDismissRequest
            )
        }
    }
}
