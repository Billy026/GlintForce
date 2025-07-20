@file:OptIn(ExperimentalMaterial3Api::class)

package com.billy.glintforce.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.data.toRemove.goalDatabase.Goal
import com.billy.glintforce.data.toRemove.goalTypeDatabase.GoalType
import com.billy.glintforce.viewModel.calendar.CalendarViewModel
import com.billy.glintforce.viewModel.settings.initiateRepo.UserPrefRepoViewModel

/**
 * Generic TopAppBar with ability to navigate backwards
 */
@Composable
fun TopAppBar(
    // Values
    modifier: Modifier = Modifier,
    title: String,
    topAppBarColor: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    color: Color? = null,
    canNavigateBack: Boolean,
    scrollBehavior: TopAppBarScrollBehavior? = null,

    // Actions
    navigateUp: () -> Unit = {},

    // ViewModels
    userPrefViewModel: UserPrefRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    val userUiState by userPrefViewModel.userPrefRepoUiState.collectAsState()

    var darkTheme by remember { mutableStateOf(false) }

    darkTheme = if (userUiState.userPrefList.isEmpty()) {
        false
    } else {
        userUiState.userPrefList.first().theme
    }

    CenterAlignedTopAppBar(title = {
        Text(
            text = title,
            color = color ?: if (darkTheme) Color.White else Color.Black,
            style = MaterialTheme.typography.labelMedium
        )
    },
        modifier = modifier,
        colors = topAppBarColor,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            }
        }
    )
}

/**
 * Composable to create a designed text field
 */
@Composable
fun CreateTextField(
    // Values
    modifier: Modifier = Modifier,
    value: String,
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,

    // Actions
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(text = stringResource(id = label)) },
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceDim
        )
    )
}

/**
 * Composable to create a designed drop-down text field
 */
@Composable
fun <T> CreateDropDown(
    // Values
    modifier: Modifier = Modifier,
    expanded: Boolean,
    value: String,
    list: List<T>,

    // Actions
    onExpandedChange: (Boolean) -> Unit,
    forEachIndexed: @Composable (Int, T) -> Unit,
    onDismissRequest: () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(),
            value = value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                focusedContainerColor = if (expanded) {
                    MaterialTheme.colorScheme.surfaceDim
                } else {
                    MaterialTheme.colorScheme.onSecondary
                }
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            list.forEachIndexed { index, item ->
                forEachIndexed(index, item)
            }
        }
    }
}

/**
 * Composable to create a floating Add button
 */
@Composable
fun FloatingAddButton(
    // Values
    contentDescription: String = "",

    // Actions
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(20.dp),
        containerColor = MaterialTheme.colorScheme.surfaceDim
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = contentDescription
        )
    }
}

/**
 * Composable to create a progress bar.
 */
@Composable
fun ProgressBar(
    // Values
    goal: Goal,
    expenseList: List<Expense>,
    max: List<GoalType>,

    // ViewModels
    calendarViewModel: CalendarViewModel = viewModel(),
    userPrefViewModel: UserPrefRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    val userUiState by userPrefViewModel.userPrefRepoUiState.collectAsState()

    var darkTheme by remember { mutableStateOf(false) }

    darkTheme = if (userUiState.userPrefList.isEmpty()) {
        false
    } else {
        userUiState.userPrefList.first().theme
    }
    // Value for total cost of all Expenses added after adding of Goal
    val currentTotal = if (
        expenseList.any {
            calendarViewModel.compareDate(expenseDate = it.date, goalDate = goal.startDate)
                    && calendarViewModel.compareTime(
                expenseTime = it.time,
                goalTime = goal.startTime
            )
        }
    ) {
        expenseList
            .filter {
                calendarViewModel.compareDate(expenseDate = it.date, goalDate = goal.startDate)
                        && calendarViewModel.compareTime(
                    expenseTime = it.time,
                    goalTime = goal.startTime
                )
            }.map { it.cost }
            .reduce { acc, d -> acc + d }
    } else 0.0

    Row(
        modifier = Modifier
            .background(if (darkTheme) Color.White else Color.Black)
            .padding(4.dp)
    ) {
        LinearProgressIndicator(
            progress = { (currentTotal / goal.amount).toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            color =
            /**
             * Different colors depending on whether Goal is maximum or minimum restriction,
             * as well as current progress of Goal
             */
            if (currentTotal / goal.amount < 0.3f) {
                if (max[0].max!!) {
                    Color.Green
                } else {
                    Color.Red
                }
            } else if (currentTotal / goal.amount < 0.7f) {
                Color(0xFFFFA500)
            } else {
                if (max[0].max!!) {
                    Color.Red
                } else {
                    Color.Green
                }
            }
        )
    }
}