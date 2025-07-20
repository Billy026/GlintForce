package com.billy.glintforce.mainApplication.settingsTab

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.billy.glintforce.mainApplication.settingsTab.settingsOptions.SettingsOptions
import com.billy.glintforce.mainApplication.settingsTab.settingsOptions.SettingsOptionsDatasource.settingsOptionsList

/**
 * Composable to show all Settings Options
 */
@Composable
fun SettingsScreen(
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        settingsOptionsList.forEach { item ->
            SettingsTab(
                settingsOptions = item,
                onClick = { onClick(item.screen) }
            )
        }
    }
}

/**
 * Composable to display individual Settings Options
 */
@Composable
private fun SettingsTab(
    // Values
    settingsOptions: SettingsOptions,

    // Actions
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextButton(onClick = onClick) {
            Text(
                text = stringResource(id = settingsOptions.title),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

/**
 * Common Settings composable to display different actions users can change in Settings
 */
@Composable
fun SettingsRow(
    // Values
    modifier: Modifier = Modifier,
    @StringRes title: Int,

    // Actions
    switchUi: @Composable () -> Unit = {},
    ui: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = title), style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.weight(1f))
            switchUi()
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ui()
        }
    }
}