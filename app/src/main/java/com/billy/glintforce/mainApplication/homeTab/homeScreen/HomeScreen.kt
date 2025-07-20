package com.billy.glintforce.mainApplication.homeTab.homeScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.billy.glintforce.R
import com.billy.glintforce.common.getMonth
import com.billy.glintforce.data.expenses.Expenses
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.mainApplication.homeTab.homeScreen.display.DisplayScreen
import com.billy.glintforce.mainApplication.homeTab.homeScreen.spending.SpendingScreen
import com.billy.glintforce.mainApplication.table.PopUpCharts
import com.billy.glintforce.viewModel.authentication.AuthState
import com.billy.glintforce.viewModel.authentication.AuthViewModel
import com.billy.glintforce.viewModel.calendar.CalendarViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalRepoViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalTypeRepoViewModel

/**
 * Composable for the basic layout of the Home Screen
 */
@Composable
fun HomeScreen(
    // Values
    modifier: Modifier = Modifier,
    navController: NavController,

    // Actions
    navigateAdd: () -> Unit,
    navigateDisplay: () -> Unit,

    // Parameters for viewModels
    calendarViewModel: CalendarViewModel = viewModel(),
    //expenseViewModel: ExpenseRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    goalViewModel: GoalRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    goalTypeViewModel: GoalTypeRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    authViewModel: AuthViewModel
) {
    // UiStates
    //val expenseUiState by expenseViewModel.expenseRepoUiState.collectAsState()
    val goalUiState by goalViewModel.goalUiState.collectAsState()
    val goalTypeUiState by goalTypeViewModel.goalTypeRepoUiState.collectAsState()
    val calendarUiState by calendarViewModel.uiState.collectAsState()
    val authState = authViewModel.authState.observeAsState()

    // When unauthenticated, navigate to login page
    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    // Variable for the display of pop-up line graph
    var dialogIsShown by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val toast = stringResource(id = R.string.emptyReport)

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        DisplayScreen(
            list = goalUiState.goalList,
            navigateDisplay = navigateDisplay,
            expenseList = listOf(),//expenseUiState.expenseList,
            goalTypeList = goalTypeUiState.goalTypeList,
            onConfirm = {
                if ( true /**
                    expenseUiState.expenseList.any {
                        getMonth(calendarUiState.fullCurrentDate) == getMonth(it.date)
                    } **/
                ) {
                    dialogIsShown = true
                } else {
                    Toast.makeText(
                        context,
                        toast,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
        SpendingScreen(
            navigateAdd = navigateAdd,
            list = listOf(), //expenseUiState.expenseList,
            checkCurrentDate = { date: String -> calendarViewModel.checkCurrentDate(date) }
        )
    }

    if (dialogIsShown) {
        PopUpCharts(
            list = /**expenseUiState.expenseList**/listOf<Expense>().filter {
                calendarUiState.fullCurrentDate.contains(
                    other = getMonth(fullDate = it.date)
                )
            },
            chart = "line",
            onConfirm = { dialogIsShown = false }
        )
    }
}