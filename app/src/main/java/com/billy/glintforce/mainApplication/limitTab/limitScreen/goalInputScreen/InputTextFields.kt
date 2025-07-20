package com.billy.glintforce.mainApplication.limitTab.limitScreen.goalInputScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.billy.glintforce.R
import com.billy.glintforce.common.CreateDropDown
import com.billy.glintforce.common.CreateTextField
import com.billy.glintforce.viewModel.limit.mainLimitPage.LimitUiState

/**
 * Input text fields for LimitScreen and EditGoalScreen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextFields(
    // Values
    modifier: Modifier = Modifier,

    // Actions
    onGoalExpanded: (Boolean) -> Unit,
    onTypeExpanded: (Boolean) -> Unit,
    onDismissRequest: (String) -> Unit,
    onGoalClick: (Int) -> Unit,
    onTypeClick: (Int) -> Unit,
    onDescValueChange: (String) -> Unit,
    onAmountValueChange: (String) -> Unit,

    // UiStates
    uiState: LimitUiState
) {
    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            CreateDropDown(
                expanded = uiState.goalOrReminderIsExpanded,
                value = uiState.GRSelectedText,
                list = uiState.goalOrReminderList,
                onExpandedChange = onGoalExpanded,
                forEachIndexed = { index, text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = { onGoalClick(index) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                },
                onDismissRequest = { onDismissRequest("goal") }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            CreateDropDown(
                expanded = uiState.typeIsExpanded,
                value = uiState.typeSelectedText,
                list =
                // DropDown of Type text field is dependent on value in Label text field
                when (uiState.goalOrReminder) {
                    stringResource(id = R.string.goals) -> uiState.goalTypeList
                    stringResource(id = R.string.reminders) -> uiState.reminderTypeList
                    else -> listOf()
                },
                onExpandedChange = onTypeExpanded,
                forEachIndexed = { index, text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = { onTypeClick(index) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                },
                onDismissRequest = { onDismissRequest("type") }
            )
        }
    }

    CreateTextField(
        modifier = modifier,
        value = uiState.desc,
        onValueChange = onDescValueChange,
        label = R.string.details,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            autoCorrectEnabled = true
        )
    )

    CreateTextField(
        modifier = modifier,
        value = uiState.amount,
        onValueChange = onAmountValueChange,
        label = R.string.amount,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}