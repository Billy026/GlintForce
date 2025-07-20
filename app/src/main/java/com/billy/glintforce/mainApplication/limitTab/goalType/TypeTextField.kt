package com.billy.glintforce.mainApplication.limitTab.goalType

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.billy.glintforce.R
import com.billy.glintforce.common.CreateDropDown
import com.billy.glintforce.common.CreateTextField
import com.billy.glintforce.viewModel.limit.goalType.GoalTypeUiState

/**
 * Input text fields for InsertTypeScreen and EditTypeScreen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeTextField(
    // Actions
    onCheckedChange: ((Boolean) -> Unit)?,
    onClick: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit,

    // UiState
    uiState: GoalTypeUiState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        CreateDropDown(
            modifier = Modifier
                .fillMaxWidth(),
            expanded = uiState.gORrIsExpanded,
            value = uiState.gORrSelectedText,
            list = uiState.gORrList,
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
    CreateTextField(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        value = uiState.type,
        onValueChange = onValueChange,
        label = R.string.type,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        )
    )
    // Switch for max/min capacity that only appears if gORr is set to "Goals"
    val goals = stringResource(id = R.string.goals)
    if (uiState.gORr == goals) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Maximum restriction? ")
            Switch(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                checked = uiState.max,
                onCheckedChange = onCheckedChange
            )
            Text(text = " *")
        }
    }
}