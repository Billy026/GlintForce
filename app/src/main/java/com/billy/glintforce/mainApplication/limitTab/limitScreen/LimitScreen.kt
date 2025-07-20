package com.billy.glintforce.mainApplication.limitTab.limitScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.mainApplication.limitTab.limitScreen.goalDisplayScreen.CurrentGoalList
import com.billy.glintforce.mainApplication.limitTab.limitScreen.goalInputScreen.InputScreen
import com.billy.glintforce.viewModel.calendar.CalendarViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalRepoViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalTypeRepoViewModel
import com.billy.glintforce.viewModel.limit.mainLimitPage.LimitViewModel

/**
 * Composable containing the main content for the Limit Tab
 */
@Composable
fun LimitScreen(
    // Values
    modifier: Modifier = Modifier,

    // Actions
    onClick: () -> Unit,
    navigateEdit: (Int) -> Unit,

    // ViewModels
    viewModel: LimitViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    //expenseRepoViewModel: ExpenseRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    goalViewModel: GoalRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    goalTypeViewModel: GoalTypeRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    calendarViewModel: CalendarViewModel = viewModel()
) {
    // UiStates
    val uiState by viewModel.uiState.collectAsState()
    val goalUiState by goalViewModel.goalUiState.collectAsState()
    val goalTypeUiState by goalTypeViewModel.goalTypeRepoUiState.collectAsState()
    val calendarUiState by calendarViewModel.uiState.collectAsState()
    //val expenseUiState by expenseRepoViewModel.expenseRepoUiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    if (uiState.goalOrReminderList.isEmpty()) {
        viewModel.initialiseGoalReminderList(
            list = listOf(
                stringResource(id = R.string.goals),
                stringResource(id = R.string.reminders)
            )
        )
    }
    if (uiState.GRSelectedText == "") {
        viewModel.resetDropDown(
            label = stringResource(id = R.string.label),
            type = stringResource(id = R.string.type)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        InputScreen(
            viewModel = viewModel,
            uiState = uiState,
            calendarUiState = calendarUiState,
            coroutineScope = coroutineScope,
            onClick = onClick
        )

        CurrentGoalList(
            modifier = Modifier.weight(1f),
            goalList = goalUiState.goalList,
            expenseList = listOf(),//expenseUiState.expenseList,
            goalTypeList = goalTypeUiState.goalTypeList,
            viewModel = viewModel,
            coroutineScope = coroutineScope,
            navigateEdit = navigateEdit
        )
    }
}