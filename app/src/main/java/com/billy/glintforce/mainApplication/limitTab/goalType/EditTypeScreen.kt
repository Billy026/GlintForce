@file:OptIn(ExperimentalMaterial3Api::class)

package com.billy.glintforce.mainApplication.limitTab.goalType

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
import com.billy.glintforce.viewModel.limit.goalType.GoalTypeViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalTypeDetailsViewModel
import kotlinx.coroutines.launch

/**
 * Object containing details on the different GoalType destinations
 */
object GoalTypeDetailsDestination {
    private const val ROUTE = "goalTypeEdit"
    const val GOALTYPEIDARG = "goalTypeId"
    const val ROUTEWITHARGS = "$ROUTE/{$GOALTYPEIDARG}"
}

/**
 * Containing composable for the main composable of EditTypeScreen
 */
@Composable
fun EditTypeScreen(
    // Values
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,

    // Actions
    onNavigateUp: () -> Unit,
    navigateBack: () -> Unit,

    // ViewModels
    viewModel: GoalTypeViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    goalTypeViewModel: GoalTypeDetailsViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    // UiStates
    val uiState by viewModel.uiState.collectAsState()
    val goalTypeUiState = goalTypeViewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val label = stringResource(id = R.string.label)

    if (uiState.gORrList.isEmpty()) {
        viewModel.initialiseList(
            list = listOf(
                stringResource(id = R.string.goals),
                stringResource(id = R.string.reminders)
            )
        )
    }
    if (uiState.gORrSelectedText == "") {
        viewModel.resetTextFields(label = stringResource(id = R.string.label))
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.editType),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        },
    ) { innerPadding ->
        val goals = stringResource(id = R.string.goals)

        GoalTypeEntryBody(
            viewModel = viewModel,
            uiState = uiState,
            buttonString = R.string.editButton,
            contentPadding = innerPadding,
            onClick = {
                if (uiState.gORr != "" &&
                    uiState.type != ""
                ) {
                    coroutineScope.launch {
                        viewModel.updateGoalType(
                            goalType = goalTypeUiState.value.goalType,
                            gORr = uiState.gORr,
                            type = uiState.type,
                            max = if (uiState.gORr == goals) {
                                uiState.max
                            } else {
                                null
                            }
                        )
                        viewModel.resetTextFields(label = label)
                    }
                    keyboardController?.hide()
                    navigateBack()
                }
            }
        )
    }
}