package com.billy.glintforce.mainApplication.screenTemplate

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.billy.glintforce.R
import com.billy.glintforce.navHosts.Navigation
import com.billy.glintforce.data.expenses.ExpenseViewModel
import com.billy.glintforce.mainApplication.homeTab.HomePopUpDialog
import com.billy.glintforce.mainApplication.tab.TabItem
import com.billy.glintforce.viewModel.authentication.AuthViewModel
import com.billy.glintforce.viewModel.calendar.CalendarViewModel
import com.billy.glintforce.viewModel.screenTemplate.GlintForceViewModel
import com.billy.glintforce.viewModel.settings.initiateRepo.UserPrefRepoViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

/**
 * Composable containing the common UI that will be in all Tabs
 */
@Composable
fun MainScreen(
    // Values
    modifier: Modifier = Modifier,
    startDestination: String,

    // Actions
    //getDirectoryClick: (Context) -> File,
    navigateLogIn: () -> Unit,
    //changeTheme: () -> Unit,

    viewModel: GlintForceViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    calendarViewModel: CalendarViewModel = viewModel(),
    expenseViewModel: ExpenseViewModel = viewModel(),
    userPrefViewModel: UserPrefRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    authViewModel: AuthViewModel = viewModel(),
) {
    // UiStates
    val uiState by viewModel.uiState.collectAsState()
    val calendarUiState by calendarViewModel.uiState.collectAsState()
    val expenseList by expenseViewModel.expenseList.collectAsStateWithLifecycle()
    val userPrefUiState by userPrefViewModel.userPrefRepoUiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        val db = Firebase.firestore

        // Initialize databases if not already initialized
        db.collection("expenses")
            .whereEqualTo("id", authViewModel.getUserId())
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.isEmpty) {
                    Log.d("Firestore", "Documents already exist!")
                } else {
                    Log.d("Firestore", "Documents do not exist.")
                    initializeDatabases(authViewModel.getUserId())
                }
            }.addOnFailureListener { e ->
                Log.w("Firestore", "Error retrieving collections", e)
            }
    }

    val navController: NavHostController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val strList = listOf(
        stringResource(id = R.string.expenditureIn),
        stringResource(id = R.string.filterDate),
        stringResource(id = R.string.filterTime),
        stringResource(id = R.string.filterCategory),
        stringResource(id = R.string.details),
        stringResource(id = R.string.amt),
        stringResource(id = R.string.pdfToast),
    )

    // Automatically shows pop-up dialog on launch of application of "Do not show again" is disabled
    if (userPrefUiState.userPrefList.isNotEmpty()) {
        if (userPrefUiState.userPrefList.first().showWidget && uiState.firstClicked) {
            viewModel.updateDialog(bool = true)
        }

        /** Attempt at automatic report generation
        if (userPrefUiState.userPrefList.first().dateGenerated == "") {
            viewModel.updateDate(date = calendarUiState.fullCurrentDate, coroutineScope = coroutineScope)
        }
        if (
            when (userPrefUiState.userPrefList.first().report) {
                7 -> calendarUiState.currentDay == "Mon" &&
                    (!userPrefUiState.userPrefList.first().dateGenerated.contains(other = calendarUiState.currentDate) ||
                     !userPrefUiState.userPrefList.first().dateGenerated.contains(other = calendarUiState.currentMonth) ||
                     !userPrefUiState.userPrefList.first().dateGenerated.contains(other = calendarUiState.currentYear))
                14 -> calendarUiState.currentDay == "Mon" &&
                     (!userPrefUiState.userPrefList.first().dateGenerated.contains(other = calendarUiState.currentDate) ||
                      !userPrefUiState.userPrefList.first().dateGenerated.contains(other = calendarUiState.currentMonth) ||
                      !userPrefUiState.userPrefList.first().dateGenerated.contains(other = calendarUiState.currentYear))
                // later
                30 -> !userPrefUiState.userPrefList.first().dateGenerated.contains(other = calendarUiState.currentMonth) ||
                      !userPrefUiState.userPrefList.first().dateGenerated.contains(other = calendarUiState.currentYear)
                else -> false
            }
        ) {
            viewModel.setGeneration(bool = false, coroutineScope = coroutineScope)
        }
        if (
            userPrefUiState.userPrefList.first().report != -1 &&
            !userPrefUiState.userPrefList.first().reportGenerated
        ) {
            viewModel.setGeneration(bool = true, coroutineScope = coroutineScope)
            viewModel.updateDate(
                date = calendarUiState.fullCurrentDate,
                coroutineScope = coroutineScope
            )
            generatePDF(
                context = context,
                list = expenseList
                    .filter { getMonth(fullDate = it.date) == calendarUiState.currentMonth },
                strList = strList,
                directory = getDirectoryClick
            )
        }
        **/
    }

    Scaffold(
        topBar = {
            if (shouldShowCommonUI(currentRoute)) {
                GlintForceAppBar(
                    navController = navController,
                    navigateLogIn = navigateLogIn,
                    viewModel = viewModel,
                    uiState = uiState,
                    authViewModel = authViewModel
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val changeTheme = {}

                Navigation(
                    navController = navController,
                    startDestination = startDestination,
                    //getDirectoryClick = getDirectoryClick,
                    changeTheme = changeTheme,
                    authViewModel = authViewModel
                )
            }

            if (shouldShowCommonUI(currentRoute)) {
                TabNavigation(
                    selectedTabIndex = uiState.selectedTabIndex,
                    onClick = { index: Int, item: TabItem ->
                        viewModel.updateTab(index)
                        viewModel.updateSettings(boolean = false)
                        navController.navigate(item.route)
                    },
                )
            }
        }
    }

    // If on Home page and pop-up dialog should be shown
    if (startDestination == "home") {
        if (uiState.showDialog) {

            HomePopUpDialog(
                onDismiss = {
                    viewModel.updateDialog(bool = false)
                    viewModel.setFirstClick()
                },
                onCheckedChange = { _ ->
                    viewModel.toggleCheckbox()
                    coroutineScope.launch {
                        viewModel.toggleShowDialog(bool = !uiState.checked)
                    }
                },
                uiState = uiState
            )
        }
    }
}

// Determines if the current route is the correct route to show TopAppBar
private fun shouldShowCommonUI(currentRoute: String?): Boolean {
    return currentRoute != "login" && currentRoute != "signup" && currentRoute != "forgotPassword"
}

// Initialize the current user's databases
private fun initializeDatabases(id: String) {
    val db = Firebase.firestore

    db.collection("expenses")
        .add(
            hashMapOf(
                "id" to id,
            )
        ).addOnSuccessListener {
            Log.d("Firestore", "Added Expenses document successfully")
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error adding Expenses document", e)
        }

    db.collection("categories")
        .add(
            hashMapOf(
                "id" to id,
            )
        ).addOnSuccessListener {
            Log.d("Firestore", "Added Categories document successfully")
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error adding Categories document", e)
        }

    db.collection("goals")
        .add(
            hashMapOf(
                "id" to id,
            )
        ).addOnSuccessListener {
            Log.d("Firestore", "Added Goals document successfully")
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error adding Goals document", e)
        }

    db.collection("goal_types")
        .add(
            hashMapOf(
                "id" to id,
            )
        ).addOnSuccessListener {
            Log.d("Firestore", "Added Goal Types document successfully")
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error adding Goal Types document", e)
        }

    db.collection("user")
        .add(
            hashMapOf(
                "id" to id,
            )
        ).addOnSuccessListener {
            Log.d("Firestore", "Added User document successfully")
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error adding User document", e)
        }

    db.collection("user_prefs")
        .add(
            hashMapOf(
                "id" to id,
            )
        ).addOnSuccessListener {
            Log.d("Firestore", "Added User Preferences document successfully")
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error adding User Preferences document", e)
        }
}