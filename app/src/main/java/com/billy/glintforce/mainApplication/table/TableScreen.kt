package com.billy.glintforce.mainApplication.table

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.viewModel.database.DatabaseUiState
import com.billy.glintforce.viewModel.database.DatabaseViewModel

/**
 * Composable for data needed for Expenditure table
 */
@Composable
fun TableScreen(
    // Values
    modifier: Modifier = Modifier,
    userId: String? = null,
    edit: ((String) -> Unit)? = null,

    // Actions
    header: @Composable () -> Unit,
    predicate: (Expense) -> Boolean,

    // ViewModels
    viewModel: DatabaseViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    //expenseRepoViewModel: ExpenseRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
) {
    // UiStates
    val uiState by viewModel.uiState.collectAsState()
    //val expenseUiState by expenseRepoViewModel.expenseRepoUiState.collectAsState()

    TableBody(
        modifier = modifier,
        userId = userId,
        edit = edit,
        header = header,
        predicate = predicate,
        uiState = uiState,
        //expenseUiState = expenseUiState,
        viewModel = viewModel
    )
}

/**
 * Composable for basic layout of Expenditure table
 */
@Composable
fun TableBody(
    // Values
    modifier: Modifier = Modifier,
    userId: String?,
    edit: ((String) -> Unit)?,

    // Actions
    header: @Composable () -> Unit,
    predicate: (Expense) -> Boolean,

    // UiStates
    uiState: DatabaseUiState,
    //expenseUiState: ExpenseRepoUiState,

    // ViewModels
    viewModel: DatabaseViewModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        header()

        Spacer(modifier = Modifier.height(16.dp))

        if (
            /**expenseUiState.expenseList**/listOf<Expense>().any(predicate)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        viewModel.updateFilter(int = 0)
                        viewModel.resetText()
                    },
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.resetFilter),
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.sortAsc),
                    modifier = Modifier.padding(4.dp)
                )
                Switch(
                    checked = uiState.ascending,
                    onCheckedChange = { viewModel.toggleAscending() }
                )
            }
            Table(
                modifier = Modifier.weight(1f),
                userId = userId,
                ascending = uiState.ascending,
                edit = edit,
                uiState = uiState
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(R.string.noExpense))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TableButtonRow(
            uiState = uiState,
            viewModel = viewModel,
            isNotEmpty = /**expenseUiState.expenseList**/listOf<Expense>().any(predicate)
        )
    }

    ConditionalPopUps(
        list = /**expenseUiState.expenseList**/listOf<Expense>().filter(predicate),
        uiState = uiState,
        viewModel = viewModel
    )
}