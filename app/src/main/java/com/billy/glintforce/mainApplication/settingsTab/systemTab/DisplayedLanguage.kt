package com.billy.glintforce.mainApplication.settingsTab.systemTab

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.common.CreateDropDown
import com.billy.glintforce.data.expenses.ExpenseViewModel
import com.billy.glintforce.mainApplication.settingsTab.SettingsRow
import com.billy.glintforce.viewModel.database.initiateRepo.CategoryRepoViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalRepoViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalTypeRepoViewModel
import com.billy.glintforce.viewModel.settings.SystemUiState
import com.billy.glintforce.viewModel.settings.SystemViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Settings row for choosing the language to be used within the app
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayedLanguage(
    onClick: (Int, String) -> Unit,
    onDismissRequest: () -> Unit,
    onExpandedChange: (Boolean) -> Unit,

    uiState: SystemUiState
) {
    SettingsRow(title = R.string.lang) {
        CreateDropDown(
            expanded = uiState.langIsExpanded,
            value = uiState.langSelectedText,
            list = uiState.langList,
            onExpandedChange = onExpandedChange,
            forEachIndexed = { index, text ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = { onClick(index, text) },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            },
            onDismissRequest = onDismissRequest
        )
    }
}

/**
 * Functions to update languages used in all preset lists throughout the application
 */
@Composable
fun UpdateListLang(
    // Values
    coroutineScope: CoroutineScope,

    // UiStates
    uiState: SystemUiState,

    // ViewModels
    viewModel: SystemViewModel,
    expenseViewModel: ExpenseViewModel = viewModel(),
    categoryRepo: CategoryRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    goalRepo: GoalRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    goalTypeRepo: GoalTypeRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
) {
    // UiStates
    val expenseList by expenseViewModel.expenseList.collectAsStateWithLifecycle()
    val categoryUiState by categoryRepo.categoryRepoUiState.collectAsState()
    val goalUiState by goalRepo.goalUiState.collectAsState()
    val goalTypeUiState by goalTypeRepo.goalTypeRepoUiState.collectAsState()

    if (uiState.categoryList[0] != stringResource(id = R.string.foodDrinks)) {
        val list = listOf(
            stringResource(id = R.string.foodDrinks),
            stringResource(id = R.string.transport),
            stringResource(id = R.string.dailyNecessities),
            stringResource(id = R.string.leisure),
            stringResource(id = R.string.misc)
        )
        val label = listOf(
            stringResource(id = R.string.goals),
            stringResource(id = R.string.reminders)
        )
        val goalTypes = listOf(
            stringResource(id = R.string.goals),
            stringResource(id = R.string.maxLimit),
            stringResource(id = R.string.setAside)
        )

        expenseList.forEach {
            coroutineScope.launch {
                viewModel.updateExpenseRepo(
                    id = 0,//it.id,
                    category = when (it.category) {
                        uiState.categoryList[0] -> list[0]
                        uiState.categoryList[1] -> list[1]
                        uiState.categoryList[2] -> list[2]
                        uiState.categoryList[3] -> list[3]
                        uiState.categoryList[4] -> list[4]
                        else -> it.category
                    }
                )
            }
        }

        categoryUiState.categoryList.forEach {
            coroutineScope.launch {
                viewModel.updateCategoryRepo(
                    id = it.id,
                    category = if (it.id < 5) list[it.id] else it.desc
                )
            }
        }

        goalUiState.goalList.forEach {
            coroutineScope.launch {
                viewModel.updateGoalRepo(
                    id = it.id,
                    label = when (it.gORr) {
                        uiState.labelList[0] -> label[0]
                        uiState.labelList[1] -> label[1]
                        else -> it.gORr
                    },
                    goalType = when (it.type) {
                        uiState.goalTypeList[0] -> goalTypes[0]
                        uiState.goalTypeList[1] -> goalTypes[1]
                        uiState.goalTypeList[2] -> goalTypes[2]
                        else -> it.type
                    }
                )
            }
        }

        goalTypeUiState.goalTypeList.forEach {
            coroutineScope.launch {
                viewModel.updateGoalTypeRepo(
                    id = it.id,
                    label = when (it.gORr) {
                        uiState.labelList[0] -> label[0]
                        uiState.labelList[1] -> label[1]
                        else -> it.gORr
                    },
                    goalType = if (it.id < 3) goalTypes[it.id] else it.type
                )
            }
        }

        viewModel.updateLists(
            categoryList = list,
            labelList = label,
            goalTypeList = goalTypes
        )
    }
}