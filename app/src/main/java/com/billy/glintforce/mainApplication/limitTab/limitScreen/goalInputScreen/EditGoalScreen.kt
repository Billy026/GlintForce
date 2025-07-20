@file:OptIn(ExperimentalMaterial3Api::class)

package com.billy.glintforce.mainApplication.limitTab.limitScreen.goalInputScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.common.TopAppBar
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalDetailsViewModel
import com.billy.glintforce.viewModel.limit.mainLimitPage.LimitViewModel
import kotlinx.coroutines.launch

/**
 * Object containing details on the different Goal destinations
 */
object GoalDetailsDestination {
    private const val ROUTE = "goalEdit"
    const val GOALIDARG = "goalId"
    const val ROUTEWITHARGS = "$ROUTE/{$GOALIDARG}"
}

/**
 * Composable for editing individual Goals
 */
@Composable
fun EditGoalScreen(
    // Values
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,

    // Actions
    onNavigateUp: () -> Unit,
    navigateBack: () -> Unit,

    // ViewModels
    viewModel: LimitViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    goalViewModel: GoalDetailsViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.editGoal),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        },
    ) { innerPadding ->
        // UiStates
        val uiState by viewModel.uiState.collectAsState()
        val goalTypeUiState = goalViewModel.uiState.collectAsState()

        val coroutineScope = rememberCoroutineScope()
        val keyboardController = LocalSoftwareKeyboardController.current
        val label = stringResource(id = R.string.label)
        val type = stringResource(id = R.string.type)

        // Updates both GoalType lists
        viewModel.getGoalTypes(
            coroutineScope = coroutineScope,
            string = stringResource(id = R.string.goals),
            goal = true
        )
        viewModel.getGoalTypes(
            coroutineScope = coroutineScope,
            string = stringResource(id = R.string.reminders),
            goal = false
        )

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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            InputFields(
                buttonString = R.string.editButton,
                uiState = uiState,
                viewModel = viewModel,
                onClick = {
                    // Allows button to be clicked only if required text fields are filled
                    if (uiState.goalOrReminder != "" &&
                        uiState.type != "" &&
                        uiState.amount != ""
                    ) {
                        coroutineScope.launch {
                            viewModel.updateGoal(
                                goal = goalTypeUiState.value.goal,
                                gORr = uiState.goalOrReminder,
                                type = uiState.type,
                                desc = uiState.desc,
                                amount = uiState.amount
                            )
                            resetTextFields(
                                viewModel = viewModel,
                                label = label,
                                type = type
                            )
                        }
                        keyboardController?.hide()
                        navigateBack()
                    }
                }
            )
        }
    }
}