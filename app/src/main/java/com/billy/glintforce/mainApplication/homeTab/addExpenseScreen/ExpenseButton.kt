package com.billy.glintforce.mainApplication.homeTab.addExpenseScreen

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.viewModel.home.AddExpenseUiState
import com.billy.glintforce.viewModel.settings.initiateRepo.UserPrefRepoViewModel

/**
 * Composable for button in AddExpenseScreen and EditExpenseScreen
 */
@Composable
fun ExpenseAddButton(
    // Values
    modifier: Modifier = Modifier,
    buttonStr: String,

    // Actions
    onClick: () -> Unit,

    // UiStates
    uiState: AddExpenseUiState,
    userPrefViewModel: UserPrefRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
) {
    val userUiState by userPrefViewModel.userPrefRepoUiState.collectAsState()

    var darkTheme by remember { mutableStateOf(false) }

    darkTheme = if (userUiState.userPrefList.isEmpty()) {
        false
    } else {
        userUiState.userPrefList.first().theme
    }

    Button(
        modifier = modifier
            .padding(8.dp)
            .defaultMinSize(minWidth = 300.dp),
        onClick = {
            if (
                uiState.category != "" &&
                uiState.desc != "" &&
                uiState.cost != ""
            ) {
                onClick()
            }
        },
        colors = ButtonDefaults.buttonColors(
            // Changes color depending if required text fields are filled
            containerColor = if (
                uiState.category != "" &&
                uiState.desc != "" &&
                uiState.cost != ""
            ) {
                if (darkTheme) Color.White else Color.Black
            } else {
                if (darkTheme) Color.DarkGray else Color.LightGray
            }
        )
    ) {
        Text(
            text = buttonStr,
            color = if (darkTheme) Color.Black else Color.White
        )
    }
}