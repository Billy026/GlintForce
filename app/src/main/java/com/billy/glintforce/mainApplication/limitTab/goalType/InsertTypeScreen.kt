@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.billy.glintforce.mainApplication.limitTab.goalType

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.common.TopAppBar
import com.billy.glintforce.viewModel.limit.goalType.GoalTypeUiState
import com.billy.glintforce.viewModel.limit.goalType.GoalTypeViewModel
import kotlinx.coroutines.launch

/**
 * Containing composable for the main composable of InsertTypeScreen
 */
@Composable
fun InsertTypeScreen(
    // Values
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,

    // Actions
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,

    // ViewModels
    viewModel: GoalTypeViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    // UiStates
    val uiState by viewModel.uiState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val label = stringResource(id = R.string.label)

    val coroutineScope = rememberCoroutineScope()

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
        topBar = {
            TopAppBar(
                modifier = modifier,
                title = stringResource(id = R.string.addType),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        val goals = stringResource(id = R.string.goals)

        GoalTypeEntryBody(
            viewModel = viewModel,
            uiState = uiState,
            buttonString = R.string.addButton,
            contentPadding = innerPadding,
            onClick = {
                if (uiState.gORr != "" &&
                    uiState.type != ""
                ) {
                    coroutineScope.launch {
                        viewModel.addGoalType(goals = goals)
                        viewModel.resetTextFields(label = label)
                    }
                    keyboardController?.hide()
                    navigateBack()
                }
            }
        )
    }
}

/**
 * Composable to display all text fields
 */
@Composable
fun GoalTypeEntryBody(
    // Values
    modifier: Modifier = Modifier,
    @StringRes buttonString: Int,
    contentPadding: PaddingValues,

    // Actions
    onClick: () -> Unit,

    // UiStates
    uiState: GoalTypeUiState,

    // ViewModels
    viewModel: GoalTypeViewModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        TypeTextField(
            onCheckedChange = { viewModel.updateSwitch() },
            onClick = { index ->
                viewModel.updateSelected(index = index)
                viewModel.setExpansion(boolean = false)
                viewModel.updateDropDown()
            },
            onDismissRequest = { viewModel.setExpansion(boolean = false) },
            onExpandedChange = { viewModel.toggleExpansion() },
            onValueChange = { viewModel.updateType(it) },
            uiState = uiState
        )

        Text(
            text = stringResource(id = R.string.requiredText),
            modifier = Modifier.padding(16.dp),
        )
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )

        TypeButton(
            buttonString = buttonString,
            onClick = onClick,
            uiState = uiState
        )
    }
}