@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.billy.glintforce.mainApplication.databaseTab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.common.TopAppBar
import com.billy.glintforce.data.expenses.editExpense
import com.billy.glintforce.mainApplication.homeTab.addExpenseScreen.ExpenseAddButton
import com.billy.glintforce.mainApplication.homeTab.addExpenseScreen.ExpenseInputFields
import com.billy.glintforce.viewModel.home.AddExpenseViewModel
import kotlinx.coroutines.launch

/**
 * Screen to edit individual Expenses
 */
@Composable
fun EditExpenseScreen(
    // Values
    modifier: Modifier = Modifier,
    param: String,
    canNavigateBack: Boolean = true,

    // Actions
    onNavigateUp: () -> Unit,
    navigateBack: () -> Unit,

    // ViewModels
    viewModel: AddExpenseViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.editExpense),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        // UiStates
        val uiState by viewModel.uiState.collectAsState()

        val coroutineScope = rememberCoroutineScope()
        val keyboardController = LocalSoftwareKeyboardController.current

        val catString = stringResource(id = R.string.filterCategory)

        LaunchedEffect(key1 = Unit) {
            viewModel.setCategories(coroutineScope = coroutineScope)

            if (uiState.categorySelectedText == "") {
                viewModel.resetTextFields(
                    categorySelectedText = "$catString *"
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExpenseInputFields(viewModel = viewModel, uiState = uiState)
            }

            Text(
                text = stringResource(id = R.string.requiredText),
                modifier = Modifier.padding(16.dp)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )

            val categoryStr = "${stringResource(id = R.string.filterCategory)} *"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ExpenseAddButton(
                    buttonStr = stringResource(R.string.editButton),
                    onClick = {
                        editExpense(
                            userId = viewModel.getUserId(),
                            expenseId = param,
                            category = uiState.category,
                            desc = uiState.desc,
                            cost = uiState.cost.toDouble()
                        )
                        coroutineScope.launch {
                            viewModel.resetTextFields(
                                categorySelectedText = categoryStr
                            )
                        }
                        keyboardController?.hide()
                        navigateBack()
                    },
                    uiState = uiState
                )
            }
        }
    }
}