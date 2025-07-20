package com.billy.glintforce.mainApplication.table

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.viewModel.database.DatabaseUiState
import com.billy.glintforce.viewModel.database.DatabaseViewModel
import java.util.Locale

/**
 * Pop Up Dialog that appears when Database button is clicked
 */
@Composable
fun ConditionalPopUps(
    // Values
    list: List<Expense>,

    // UiStates
    uiState: DatabaseUiState,

    // ViewModels
    viewModel: DatabaseViewModel
) {
    if (uiState.dialogShown) {
        when (uiState.dialogId) {
            // Filter by date
            "date" -> {
                DatePickerWithDialog(
                    onClick = { dateToString ->
                        viewModel.setDialog(bool = false)
                        if (dateToString != "") {
                            viewModel.saveDate(date = dateToString, filter = true)
                            viewModel.updateFilter(int = 1)
                        }
                    },
                    onDismiss = { viewModel.setDialog(bool = false) }
                )
            }
            // Filter by Category
            "category" -> {
                PopUpDialog(
                    input = true,
                    text = stringResource(id = R.string.selectCategory),
                    label = R.string.filterCategory,
                    onConfirm = {
                        if (uiState.category != "") {
                            viewModel.updateFilter(int = 2)
                        }
                        viewModel.setDialog(bool = false)
                        viewModel.resetSelected()
                    },
                    onDismiss = {
                        viewModel.setDialog(bool = false)
                        viewModel.resetSelected()
                    },
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
            // Filter by cost range
            "cost" -> {
                PopUpDialog(
                    input = true,
                    text = stringResource(id = R.string.selectMinMax),
                    label = R.string.filterMaxCost,
                    onConfirm = {
                        if (uiState.minCost != "" || uiState.maxCost != "") {
                            viewModel.updateFilter(int = 3)
                        }
                        viewModel.setDialog(bool = false)
                        viewModel.resetSelected()
                    },
                    onDismiss = {
                        viewModel.setDialog(bool = false)
                        viewModel.resetSelected()
                    },
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
            // Calculate total cost
            "displayTotal" -> {
                // total cost
                val total = list.map { it.cost }.reduce { acc, cost -> acc + cost }

                PopUpDialog(
                    input = false,
                    text = "${stringResource(id = R.string.totalText1)} ${
                        String.format(
                            Locale.getDefault(),
                            "%.2f",
                            total
                        )
                    } ${stringResource(id = R.string.totalText2)}",
                    label = null,
                    onDismiss = {
                        viewModel.setDialog(bool = false)
                    },
                    onConfirm = { viewModel.setDialog(bool = false) },
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
            // Calculate average cost
            "displayAve" -> {
                // average cost
                var ave = list.map { it.cost }.reduce { acc, cost -> acc + cost }
                ave /= list.count()

                PopUpDialog(
                    input = false,
                    text = "${stringResource(id = R.string.aveText1)} ${
                        String.format(
                            Locale.getDefault(),
                            "%.2f",
                            ave
                        )
                    } ${stringResource(id = R.string.aveText2)}",
                    label = null,
                    onDismiss = {
                        viewModel.setDialog(bool = false)
                    },
                    onConfirm = { viewModel.setDialog(bool = false) },
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
            // Calculate by date
            "calcDate" -> {
                DatePickerWithDialog(
                    onClick = { dateToString ->
                        viewModel.setDialog(bool = false)
                        if (dateToString != "") {
                            viewModel.saveDate(date = dateToString, filter = false)
                            viewModel.setDialogId(string = "displayDate")
                            viewModel.setDialog(bool = true)
                        }
                    },
                    onDismiss = { viewModel.setDialog(bool = false) }
                )
            }
            // Display calculate by date results
            "displayDate" -> {
                // total cost on selected date
                val total = if (list.any { it.date == uiState.byDate }) {
                    list.filter { it.date == uiState.byDate }
                        .map { it.cost }
                        .reduce { acc, cost -> acc + cost }
                } else {
                    0.0
                }

                PopUpDialog(
                    input = false,
                    text = "${stringResource(id = R.string.dateText1)} ${
                        String.format(
                            Locale.getDefault(),
                            "%.2f",
                            total
                        )
                    } ${stringResource(id = R.string.dateText2)} ${uiState.byDate}",
                    label = null,
                    onDismiss = {
                        viewModel.setDialog(bool = false)
                        viewModel.resetText()
                    },
                    onConfirm = {
                        viewModel.setDialog(bool = false)
                        viewModel.resetText()
                    },
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
            // Calculate by Category
            "calcCategory" -> {
                PopUpDialog(
                    input = true,
                    text = stringResource(id = R.string.selectCategory),
                    label = R.string.filterCategory,
                    onConfirm = {
                        viewModel.setDialog(bool = false)
                        if (uiState.category != "") {
                            viewModel.setDialogId("displayCategory")
                            viewModel.setDialog(bool = true)
                        }
                    },
                    onDismiss = {
                        viewModel.setDialog(bool = false)
                        viewModel.resetSelected()
                    },
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
            // Display calculate by Category results
            "displayCategory" -> {
                // total cost on selected Category
                var total = 0.0
                list.filter { it.category == uiState.category }.forEach { total += it.cost }

                PopUpDialog(
                    input = false,
                    text = "${stringResource(id = R.string.categoryText1)} ${
                        String.format(
                            Locale.getDefault(),
                            "%.2f",
                            total
                        )
                    } ${stringResource(id = R.string.categoryText2)} ${uiState.category}",
                    label = null,
                    onDismiss = {
                        viewModel.setDialog(bool = false)
                        viewModel.resetSelected()
                    },
                    onConfirm = {
                        viewModel.setDialog(bool = false)
                        viewModel.resetSelected()
                    },
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
            // Bar chart model
            "barModel" -> {
                PopUpCharts(
                    list = list,
                    chart = "bar",
                    onConfirm = {
                        viewModel.setDialog(bool = false)
                        viewModel.resetSelected()
                    }
                )
            }
            // Line chart model
            "lineModel" -> {
                PopUpCharts(
                    list = list,
                    chart = "line",
                    onConfirm = {
                        viewModel.setDialog(bool = false)
                        viewModel.resetSelected()
                    }
                )
            }
            // Pie chart model
            "pieModel" -> {
                PopUpCharts(
                    list = list,
                    chart = "pie",
                    onConfirm = {
                        viewModel.setDialog(bool = false)
                        viewModel.resetSelected()
                    }
                )
            }

            else -> {}
        }
    }
}