@file:OptIn(ExperimentalMaterial3Api::class)

package com.billy.glintforce.mainApplication.homeTab.addExpenseScreen

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
import com.billy.glintforce.data.expenses.addExpense
import com.billy.glintforce.viewModel.calendar.CalendarViewModel
import com.billy.glintforce.viewModel.home.AddExpenseViewModel
import kotlinx.coroutines.launch

/**
 * Composable for screen to add new Expense
 */
@Composable
fun AddExpenseScreen(
    // Values
    modifier: Modifier = Modifier,

    // Actions
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    navigateBack: () -> Unit,

    // ViewModels
    viewModel: AddExpenseViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    calendarViewModel: CalendarViewModel = viewModel(),
) {
    // UiStates
    val uiState by viewModel.uiState.collectAsState()
    val calendarUiState by calendarViewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val categoryStr = "${stringResource(id = R.string.filterCategory)} *"

    val initCategoryText = com.billy.glintforce.mainApplication.UiText.StringResource(R.string.filterCategory).asString()

    LaunchedEffect(key1 = Unit) {
        // Delete once cloud storage is set up
        viewModel.setCategories(coroutineScope = coroutineScope)

        viewModel.resetTextFields(
            categorySelectedText = "$initCategoryText *"
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.addExpense),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExpenseInputFields(
                    viewModel = viewModel,
                    uiState = uiState
                )
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ExpenseAddButton(
                    buttonStr = stringResource(id = R.string.addButton),
                    onClick = {
                        addExpense(
                            userId = viewModel.getUserId(),
                            category = uiState.category, desc = uiState.desc,
                            cost = uiState.cost.toDouble(),
                            date = calendarUiState.fullCurrentDate,
                            time = calendarUiState.time,
                            month = calendarUiState.shortCurrentDate
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