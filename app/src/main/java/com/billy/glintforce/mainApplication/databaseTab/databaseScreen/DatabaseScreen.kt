package com.billy.glintforce.mainApplication.databaseTab.databaseScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.mainApplication.databaseTab.databaseScreen.table.DatabaseHeader
import com.billy.glintforce.mainApplication.table.TableScreen
import com.billy.glintforce.viewModel.calendar.CalendarViewModel
import com.billy.glintforce.viewModel.home.AddExpenseViewModel

/**
 * Feedback:
 * - combine date and time
 * - make edit and delete icons on click
 * - desc on hold
 * - split database into daily and monthly
 */

/**
 * Composable for data needed for Database screen
 */
@Composable
fun DatabaseScreen(
    // Values
    modifier: Modifier = Modifier,

    // Actions
    navigateEdit: (String) -> Unit,
    navigateManage: () -> Unit,

    // ViewModels
    viewModel: AddExpenseViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    calendarViewModel: CalendarViewModel = viewModel()
) {
    // UiStates
    val calendarUiState by calendarViewModel.uiState.collectAsState()

    TableScreen(
        modifier = modifier,
        userId = viewModel.getUserId(),
        edit = navigateEdit,
        header = {
            DatabaseHeader(
                shortDate = calendarUiState.shortCurrentDate,
                navigateManage = navigateManage
            )
        },
        predicate = { it.month == calendarUiState.shortCurrentDate }
    )
}
