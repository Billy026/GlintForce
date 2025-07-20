package com.billy.glintforce.mainApplication.homeTab.addExpenseScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.billy.glintforce.R
import com.billy.glintforce.common.CreateDropDown
import com.billy.glintforce.common.CreateTextField
import com.billy.glintforce.viewModel.home.AddExpenseUiState
import com.billy.glintforce.viewModel.home.AddExpenseViewModel

/**
 * Composable for input fields in AddExpenseScreen and EditExpenseScreen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseInputFields(
    // ViewModels
    viewModel: AddExpenseViewModel,

    // UiStates
    uiState: AddExpenseUiState
) {
    Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp)) {
        CreateDropDown(
            modifier = Modifier
                .fillMaxWidth(),
            expanded = uiState.categoryIsExpanded,
            value = uiState.categorySelectedText,
            list = uiState.categoryList,
            onExpandedChange = { viewModel.toggleExpansion() },
            forEachIndexed = { index, text ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        viewModel.updateSelected(index = index)
                        viewModel.setExpansion(bool = false)
                        viewModel.updateDropDown()
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            },
            onDismissRequest = { viewModel.setExpansion(bool = false) }
        )
    }

    CreateTextField(
        value = uiState.desc,
        onValueChange = { viewModel.updateDesc(it) },
        label = R.string.detailsRequired,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            autoCorrectEnabled = true
        )
    )

    CreateTextField(
        value = uiState.cost,
        onValueChange = { viewModel.updateCost(it) },
        label = R.string.costRequired,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}