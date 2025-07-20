package com.billy.glintforce.mainApplication.limitTab.limitScreen.goalInputScreen

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.viewModel.calendar.CalendarUiState
import com.billy.glintforce.viewModel.limit.mainLimitPage.LimitUiState
import com.billy.glintforce.viewModel.limit.mainLimitPage.LimitViewModel
import com.billy.glintforce.viewModel.settings.initiateRepo.UserPrefRepoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Composable for the Goal Input section of the Limit Tab
 */
@Composable
fun InputScreen(
    // Values
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,

    // Actions
    onClick: () -> Unit,

    // UiStates
    uiState: LimitUiState,
    calendarUiState: CalendarUiState,

    // ViewModels
    viewModel: LimitViewModel,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val label = stringResource(id = R.string.label)
    val type = stringResource(id = R.string.type)

    // Update both GoalType lists
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LimitHeader(onClick = onClick)

        InputFields(
            buttonString = R.string.addButton,
            uiState = uiState,
            viewModel = viewModel,
            onClick = {
                // Allows button to be clicked only if required text fields are filled
                if (uiState.goalOrReminder != "" &&
                    uiState.type != "" &&
                    uiState.amount != ""
                ) {
                    coroutineScope.launch {
                        viewModel.addToCurrent(
                            date = calendarUiState.fullCurrentDate,
                            time = calendarUiState.time
                        )
                        resetTextFields(
                            viewModel = viewModel,
                            label = label,
                            type = type
                        )
                    }
                    keyboardController?.hide()
                }
            }
        )
    }
}

/**
 * Composable containing all text fields
 */
@Composable
fun InputFields(
    // Values
    modifier: Modifier = Modifier,
    @StringRes buttonString: Int,

    // Actions
    onClick: () -> Unit,

    // UiStates
    uiState: LimitUiState,

    // ViewModels
    viewModel: LimitViewModel,
    userPrefViewModel: UserPrefRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    val userUiState by userPrefViewModel.userPrefRepoUiState.collectAsState()

    var darkTheme by remember { mutableStateOf(false) }

    darkTheme = if (userUiState.userPrefList.isEmpty()) {
        false
    } else {
        userUiState.userPrefList.first().theme
    }
    val context = LocalContext.current
    val toastMsg = stringResource(id = R.string.toastMsg)
    val type = stringResource(id = R.string.type)
    val goals = stringResource(id = R.string.goals)

    InputTextFields(
        onGoalExpanded = { viewModel.toggleExpansion("goal") },
        onTypeExpanded = {
            // If Label text field is empty, informs user to input Label first
            if (viewModel.checkCurrentEmpty()) {
                Toast.makeText(
                    context,
                    toastMsg,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.toggleExpansion("type")
            }
        },
        onDismissRequest = { str ->
            viewModel.setExpansion(string = str, boolean = false)
        },
        onGoalClick = { index ->
            viewModel.updateSelected(
                index = index,
                type = false,
                goalStr = goals
            )
            viewModel.setExpansion(string = "goal", boolean = false)
            viewModel.updateDropDown(type = "goal")
            viewModel.resetType(type = type)
        },
        onTypeClick = { index ->
            viewModel.updateSelected(
                index = index,
                type = true,
                goalStr = goals
            )
            viewModel.setExpansion(string = "type", boolean = false)
            viewModel.updateDropDown(type = "type")
        },
        onDescValueChange = { viewModel.updateDesc(it) },
        onAmountValueChange = { viewModel.updateAmount(it) },
        uiState = uiState
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
    ) {
        Text(text = stringResource(id = R.string.requiredText))
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                // Changes color depending if required text fields are filled
                containerColor = if (
                    uiState.goalOrReminder != "" &&
                    uiState.type != "" &&
                    uiState.amount != ""
                ) {
                    if (darkTheme) Color.White else Color.Black
                } else {
                    if (darkTheme) Color.DarkGray else Color.LightGray
                }
            ),
            modifier = Modifier
                .padding(8.dp)
                .defaultMinSize(minWidth = 300.dp)
        ) {
            Text(
                text = stringResource(id = buttonString),
                color = if (darkTheme) Color.Black else Color.White
            )
        }
    }
}

/**
 * Header for input screen of Limit page
 */
@Composable
private fun LimitHeader(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.limitScreen),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(id = R.string.manageType),
                color = Color.White
            )
        }
    }
}

/**
 * Clears values in all text fields once Add button is pressed
 */
fun resetTextFields(
    // Values
    label: String,
    type: String,

    // ViewModels
    viewModel: LimitViewModel
) {
    viewModel.resetDropDown(
        label = label,
        type = type
    )
    viewModel.updateDesc("")
    viewModel.updateAmount("")
}