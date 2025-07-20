@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.billy.glintforce.mainApplication.homeTab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.goalDatabase.Goal
import com.billy.glintforce.common.TopAppBar
import com.billy.glintforce.viewModel.home.DisplayGoalUiState
import com.billy.glintforce.viewModel.home.DisplayGoalViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalRepoViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalUiState
import kotlinx.coroutines.launch

/**
 * Composable to show available Goals and Reminders to display
 */
@Composable
fun DisplayGoalScreen(
    // Values
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,

    // Actions
    onNavigateUp: () -> Unit,

    // ViewModels
    viewModel: DisplayGoalViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    goalViewModel: GoalRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.displayGoals),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        // UiStates
        val uiState by viewModel.uiState.collectAsState()
        val goalUiState by goalViewModel.goalUiState.collectAsState()

        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SelectAllRow(
                onCheckedChange = {
                    viewModel.updateAll(bool = it)
                    coroutineScope.launch {
                        viewModel.selectAllChange(
                            list = goalUiState.goalList
                        )
                    }
                },
                uiState = uiState
            )

            GoalsLazyColumn(
                onCheckedChange = { it, goal ->
                    coroutineScope.launch {
                        viewModel.updateDisplay(
                            goal = goal,
                            bool = it
                        )
                    }
                },
                goalUiState = goalUiState
            )
        }
    }
}

/**
 * Row for select all checkbox
 */
@Composable
private fun SelectAllRow(
    // Actions
    onCheckedChange: (Boolean) -> Unit,

    // UiState
    uiState: DisplayGoalUiState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.selectAll))
        Checkbox(
            checked = uiState.selectAll,
            onCheckedChange = onCheckedChange
        )
    }
}

/**
 * Composable to display all selectable Goals and Reminders
 */
@Composable
private fun GoalsLazyColumn(
    // Actions
    onCheckedChange: (Boolean, Goal) -> Unit,

    // UiStates
    goalUiState: GoalUiState
) {
    val goals = stringResource(id = R.string.goals)
    val reminders = stringResource(id = R.string.reminders)

    LazyColumn {
        items(
            items = goalUiState.goalList
                .filter { it.gORr == goals }
                .sortedBy { it.type }
        ) { goal ->
            DisplayRow(
                modifier = Modifier.padding(8.dp),
                goal = goal,
                onCheckedChange = { onCheckedChange(it, goal) }
            )
        }

        items(
            items = goalUiState.goalList
                .filter { it.gORr == reminders }
                .sortedBy { it.type }
        ) { goal ->
            DisplayRow(
                modifier = Modifier.padding(8.dp),
                goal = goal,
                onCheckedChange = { onCheckedChange(it, goal) }
            )
        }
    }
}

/**
 * Row to display checkboxes for individual Goals and Reminders
 */
@Composable
private fun DisplayRow(
    // Values
    modifier: Modifier = Modifier,
    goal: Goal,

    // Actions
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = goal.display, onCheckedChange = onCheckedChange)
        Text(
            text = "${goal.gORr}: ${goal.type}${
                if (goal.desc != "") {
                    " - ${goal.desc}"
                } else {
                    ""
                }
            }"
        )
    }
}