@file:OptIn(ExperimentalMaterial3Api::class)

package com.billy.glintforce.mainApplication.limitTab.goalType

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.goalTypeDatabase.GoalType
import com.billy.glintforce.common.FloatingAddButton
import com.billy.glintforce.common.TopAppBar
import com.billy.glintforce.viewModel.limit.goalType.GoalTypeViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalTypeRepoUiState
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalTypeRepoViewModel
import com.billy.glintforce.viewModel.settings.initiateRepo.UserPrefRepoViewModel
import kotlinx.coroutines.launch

/**
 * Containing composable for the main composable of ManageTypeScreen
 */
@Composable
fun ManageTypeScreen(
    // Values
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,

    // Actions
    navigateAdd: () -> Unit,
    navigateEdit: (Int) -> Unit,
    onNavigateUp: () -> Unit,

    // ViewModels
    viewModel: GoalTypeViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    repoViewModel: GoalTypeRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    userPrefViewModel: UserPrefRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    val userUiState by userPrefViewModel.userPrefRepoUiState.collectAsState()

    var darkTheme by remember { mutableStateOf(false) }

    darkTheme = if (userUiState.userPrefList.isEmpty()) {
        false
    } else {
        userUiState.userPrefList.first().theme
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.manageType),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        },
        floatingActionButton = {
            FloatingAddButton(
                onClick = navigateAdd,
                contentDescription = "Add GoalType"
            )
        }
    ) { innerPadding ->
        // UiStates
        val goalRepoUiState by repoViewModel.goalTypeRepoUiState.collectAsState()

        val coroutineScope = rememberCoroutineScope()

        ManageTypeEntryBody(
            modifier = modifier,
            goalRepo = goalRepoUiState,
            navigateEdit = navigateEdit,
            contentPadding = innerPadding,
            onDelete = { goalType ->
                coroutineScope.launch {
                    viewModel.deleteGoalType(goalType)
                }
            },
            darkTheme = darkTheme
        )
    }
}

/**
 * Composable that deals with if GoalType list is empty
 */
@Composable
private fun ManageTypeEntryBody(
    // Values
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    darkTheme: Boolean,

    // Actions
    navigateEdit: (Int) -> Unit,
    onDelete: (GoalType) -> Unit,

    // UiStates
    goalRepo: GoalTypeRepoUiState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Displays items in GoalType list
        GoalTypeDisplay(
            list = goalRepo.goalTypeList,
            navigateEdit = navigateEdit,
            onDelete = onDelete,
            darkTheme = darkTheme
        )
    }
}