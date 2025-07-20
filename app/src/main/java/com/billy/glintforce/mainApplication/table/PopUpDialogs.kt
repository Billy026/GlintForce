package com.billy.glintforce.mainApplication.table

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.common.CreateDropDown
import com.billy.glintforce.common.CreateTextField
import com.billy.glintforce.mainApplication.databaseTab.charts.BarChartDiagram
import com.billy.glintforce.mainApplication.databaseTab.charts.LineChartDiagram
import com.billy.glintforce.mainApplication.databaseTab.charts.PieChartDiagram
import com.billy.glintforce.viewModel.calendar.DateUtils
import com.billy.glintforce.viewModel.database.DatabaseUiState
import com.billy.glintforce.viewModel.database.DatabaseViewModel
import com.billy.glintforce.viewModel.database.initiateRepo.CategoryRepoViewModel

/**
 * Simple Pop Up Dialog for Database buttons
 */
@Composable
fun PopUpDialog(
    // Values
    modifier: Modifier = Modifier,
    input: Boolean,
    text: String,
    @StringRes label: Int?,

    // Actions
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,

    // UiStates
    uiState: DatabaseUiState,

    // ViewModels
    viewModel: DatabaseViewModel
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.95f)
                .border(1.dp, color = Color.Black, RoundedCornerShape(15.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = text, modifier = Modifier.padding(15.dp))
                if (input) {
                    // For Dialogs requiring Category input
                    when (label!!) {
                        R.string.filterCategory -> {
                            viewModel.setCategories(coroutineScope = rememberCoroutineScope())

                            CreateDropDown(
                                expanded = uiState.categoryExpanded,
                                value = uiState.categorySelected,
                                list = uiState.categoryList,
                                onExpandedChange = { viewModel.toggleExpansion() },
                                forEachIndexed = { index, text ->
                                    DropdownMenuItem(
                                        text = { Text(text = text) },
                                        onClick = {
                                            viewModel.updateSelected(index = index)
                                            viewModel.toggleExpansion()
                                            viewModel.updateDropDown()
                                        }
                                    )
                                },
                                onDismissRequest = { viewModel.toggleExpansion() }
                            )
                        }
                        // For dialogs requiring max/min cost inputs
                        R.string.filterMaxCost -> {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                CreateTextField(
                                    value = uiState.minCost,
                                    onValueChange = {
                                        viewModel.updateText(
                                            string = "minCost",
                                            int = it
                                        )
                                    },
                                    label = R.string.filterMinCost,
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    )
                                )
                                CreateTextField(
                                    value = uiState.maxCost,
                                    onValueChange = {
                                        viewModel.updateText(
                                            string = "maxCost",
                                            int = it
                                        )
                                    },
                                    label = label,
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    )
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (input) {
                    Button(onClick = onDismiss) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(50.dp))
                }
                Button(onClick = onConfirm) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            }
        }
    }
}

/**
 * Pop Up Dialog with date picker
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerWithDialog(
    // Values
    modifier: Modifier = Modifier,

    // Actions
    onClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val dateState = rememberDatePickerState()
    val millisToLocalDate = dateState.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }
    val dateToString = millisToLocalDate?.let {
        DateUtils().dateToString(millisToLocalDate)
    } ?: ""

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onClick(dateToString) }
            ) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = dateState,
            dateFormatter = DatePickerDefaults.dateFormatter(
                selectedDateSkeleton = "dd MMMM yyyy",
            )
        )
    }
}

/**
 * Pop Up Dialog with chart
 */
@Composable
fun PopUpCharts(
    // Values
    modifier: Modifier = Modifier,
    list: List<Expense>,
    chart: String,

    // Actions
    onConfirm: () -> Unit,

    // ViewModels
    categoryViewModel: CategoryRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
) {
    val categoryUiState by categoryViewModel.categoryRepoUiState.collectAsState()

    Dialog(
        onDismissRequest = onConfirm,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(if (chart == "pie") 0.7f else 0.95f)
                .border(1.dp, color = Color.Black, RoundedCornerShape(15.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            when (chart) {
                "bar" -> BarChartDiagram(
                    list = list,
                    categoryList = categoryUiState.categoryList,
                    onConfirm = onConfirm
                )

                "line" -> LineChartDiagram(
                    list = list,
                    onConfirm = onConfirm,
                )

                "pie" -> PieChartDiagram(
                    list = list,
                    onConfirm = onConfirm
                )

                else -> {}
            }
        }
    }
}