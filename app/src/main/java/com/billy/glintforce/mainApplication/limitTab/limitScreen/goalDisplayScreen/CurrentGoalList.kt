package com.billy.glintforce.mainApplication.limitTab.limitScreen.goalDisplayScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.data.toRemove.goalDatabase.Goal
import com.billy.glintforce.data.toRemove.goalTypeDatabase.GoalType
import com.billy.glintforce.common.TopAppBar
import com.billy.glintforce.viewModel.limit.mainLimitPage.LimitViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Composable containing the display of goals
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentGoalList(
    // Values
    modifier: Modifier = Modifier,
    goalList: List<Goal>,
    expenseList: List<Expense>,
    goalTypeList: List<GoalType>,
    coroutineScope: CoroutineScope,

    // Actions
    navigateEdit: (Int) -> Unit,

    // ViewModels
    viewModel: LimitViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = stringResource(id = R.string.current),
                    topAppBarColor = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    canNavigateBack = false,
                    scrollBehavior = scrollBehavior
                )
            },
            containerColor = Color.Transparent,
        ) {
            // If no goals, display message
            if (goalList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.goalsEmpty),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            } else {
                // If goals available, display them
                GoalAndReminderList(
                    goalList = goalList,
                    expenseList = expenseList,
                    goalTypeList = goalTypeList,
                    contentPadding = it,
                    viewModel = viewModel,
                    coroutineScope = coroutineScope,
                    navigateEdit = navigateEdit
                )
            }
        }
    }
}

/**
 * Composable for the display of goals and reminders
 */
@Composable
private fun GoalAndReminderList(
    // Values
    modifier: Modifier = Modifier,
    goalList: List<Goal>,
    expenseList: List<Expense>,
    goalTypeList: List<GoalType>,
    contentPadding: PaddingValues,
    coroutineScope: CoroutineScope,

    // Actions
    navigateEdit: (Int) -> Unit,

    // ViewModels
    viewModel: LimitViewModel
) {
    Column(
        modifier = modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        OverallGoalsCard(
            goalList = goalList,
            goalTypeList = goalTypeList,
            expenseList = expenseList,
            navigateEdit = navigateEdit,
            onClick = { goal ->
                coroutineScope.launch {
                    viewModel.delete(goal = goal)
                }
            }
        )

        OverallRemindersCard(
            goalList = goalList,
            navigateEdit = navigateEdit,
            onClick = { reminder ->
                coroutineScope.launch {
                    viewModel.delete(goal = reminder)
                }
            }
        )
    }
}

/**
 * Generic composable for titled cards with Edit and Delete functions
 */
@Composable
fun CreateCard(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    navigateOnClick: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            text()
        }
        IconButton(onClick = navigateOnClick) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            )
        }
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
        }
    }
}