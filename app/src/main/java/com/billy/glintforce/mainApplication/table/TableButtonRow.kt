package com.billy.glintforce.mainApplication.table

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.billy.glintforce.R
import com.billy.glintforce.viewModel.database.DatabaseUiState
import com.billy.glintforce.viewModel.database.DatabaseViewModel

/**
 * The four buttons in Database page and Reports page
 */
@Composable
fun TableButtonRow(
    // Values
    modifier: Modifier = Modifier,
    isNotEmpty: Boolean,

    // UiStates
    uiState: DatabaseUiState,

    // ViewModels
    viewModel: DatabaseViewModel
) {
    val context = LocalContext.current
    val toast = stringResource(id = R.string.emptyReport)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Sort button
        CreateDropDownButton(
            text = stringResource(id = R.string.sort),
            index = 0,
            uiState = uiState,
            list = uiState.sortList.map {
                Pair(it.first.asString(), it.second)
            },
            setExpansion = { bool -> viewModel.setExpansion(buttonIndex = 0, bool = bool) },
            onClick = { int ->
                if (isNotEmpty) {
                    viewModel.updateSort(int = int)
                    viewModel.setExpansion(buttonIndex = 0, bool = false)
                } else {
                    viewModel.setExpansion(buttonIndex = 0, bool = false)
                    Toast.makeText(
                        context,
                        toast,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
        // Filter button
        CreateDropDownButton(
            text = stringResource(id = R.string.filter),
            index = 1,
            uiState = uiState,
            list = uiState.filterList.map {
                Pair(it.first.asString(), it.second)
            },
            setExpansion = { bool -> viewModel.setExpansion(buttonIndex = 1, bool = bool) },
            onClick = {
                if (isNotEmpty) {
                    when (it) {
                        1 -> {
                            viewModel.setDialogId(string = "date")
                            viewModel.setDialog(bool = true)
                            viewModel.setExpansion(buttonIndex = 1, bool = false)
                        }

                        2 -> {
                            viewModel.setDialogId(string = "category")
                            viewModel.setDialog(bool = true)
                            viewModel.setExpansion(buttonIndex = 1, bool = false)
                        }

                        else -> {
                            viewModel.setDialogId(string = "cost")
                            viewModel.setDialog(bool = true)
                            viewModel.setExpansion(buttonIndex = 1, bool = false)
                        }
                    }
                } else {
                    viewModel.setExpansion(buttonIndex = 1, bool = false)
                    Toast.makeText(
                        context,
                        toast,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Calculate button
        CreateDropDownButton(
            text = stringResource(id = R.string.calculate),
            index = 2,
            uiState = uiState,
            list = uiState.calculateList.map {
                Pair(it.first.asString(), it.second)
            },
            setExpansion = { bool -> viewModel.setExpansion(buttonIndex = 2, bool = bool) },
            onClick = {
                if (isNotEmpty) {
                    when (it) {
                        0 -> {
                            viewModel.setDialogId(string = "displayTotal")
                            viewModel.setDialog(bool = true)
                            viewModel.setExpansion(buttonIndex = 2, bool = false)
                        }

                        1 -> {
                            viewModel.setDialogId(string = "displayAve")
                            viewModel.setDialog(bool = true)
                            viewModel.setExpansion(buttonIndex = 2, bool = false)
                        }

                        2 -> {
                            viewModel.setDialogId(string = "calcDate")
                            viewModel.setDialog(bool = true)
                            viewModel.setExpansion(buttonIndex = 2, bool = false)
                        }

                        else -> {
                            viewModel.setDialogId(string = "calcCategory")
                            viewModel.setDialog(bool = true)
                            viewModel.setExpansion(buttonIndex = 2, bool = false)
                        }
                    }
                } else {
                    viewModel.setExpansion(buttonIndex = 2, bool = false)
                    Toast.makeText(
                        context,
                        toast,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
        // Model button
        CreateDropDownButton(
            text = stringResource(id = R.string.model),
            index = 3,
            list = uiState.modelList.map {
                Pair(it.first.asString(), it.second)
            },
            setExpansion = { bool -> viewModel.setExpansion(buttonIndex = 3, bool = bool) },
            uiState = uiState,
            onClick = {
                if (isNotEmpty) {
                    when (it) {
                        0 -> {
                            viewModel.setDialogId(string = "barModel")
                            viewModel.setDialog(bool = true)
                            viewModel.setExpansion(buttonIndex = 3, bool = false)
                        }

                        1 -> {
                            viewModel.setDialogId(string = "lineModel")
                            viewModel.setDialog(bool = true)
                            viewModel.setExpansion(buttonIndex = 3, bool = false)
                        }

                        else -> {
                            viewModel.setDialogId(string = "pieModel")
                            viewModel.setDialog(bool = true)
                            viewModel.setExpansion(buttonIndex = 3, bool = false)
                        }
                    }
                } else {
                    viewModel.setExpansion(buttonIndex = 3, bool = false)
                    Toast.makeText(
                        context,
                        toast,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
        )
    }
}

/**
 * Composable to create a designed drop-down button
 */
@Composable
private fun CreateDropDownButton(
    // Values
    modifier: Modifier = Modifier,
    text: String,
    index: Int,
    list: List<Pair<String, Int>>,

    // Actions
    onClick: (Int) -> Unit,
    setExpansion: (Boolean) -> Unit,

    // UiStates
    uiState: DatabaseUiState
) {
    Box(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier.width(130.dp),
            onClick = { setExpansion(true) }
        ) {
            Text(
                text = text,
                color = Color.White
            )
        }

        DropdownMenu(
            expanded = when (index) {
                0 -> uiState.sortExpanded
                1 -> uiState.filterExpanded
                2 -> uiState.calculateExpanded
                else -> uiState.modelExpanded
            },
            onDismissRequest = { setExpansion(false) }
        ) {
            list.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.first) },
                    onClick = { onClick(it.second) }
                )
            }
        }
    }
}