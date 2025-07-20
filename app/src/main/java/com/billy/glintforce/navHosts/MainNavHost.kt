package com.billy.glintforce.navHosts

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.billy.glintforce.mainApplication.databaseTab.EditExpenseScreen
import com.billy.glintforce.mainApplication.databaseTab.categories.CategoryDetailsDestination
import com.billy.glintforce.mainApplication.databaseTab.categories.EditCategoryScreen
import com.billy.glintforce.mainApplication.databaseTab.categories.InsertCategoryScreen
import com.billy.glintforce.mainApplication.databaseTab.categories.ManageCategoriesScreen
import com.billy.glintforce.mainApplication.databaseTab.databaseScreen.DatabaseScreen
import com.billy.glintforce.mainApplication.homeTab.DisplayGoalScreen
import com.billy.glintforce.mainApplication.homeTab.addExpenseScreen.AddExpenseScreen
import com.billy.glintforce.mainApplication.homeTab.homeScreen.HomeScreen
import com.billy.glintforce.mainApplication.limitTab.goalType.EditTypeScreen
import com.billy.glintforce.mainApplication.limitTab.goalType.GoalTypeDetailsDestination
import com.billy.glintforce.mainApplication.limitTab.goalType.InsertTypeScreen
import com.billy.glintforce.mainApplication.limitTab.goalType.ManageTypeScreen
import com.billy.glintforce.mainApplication.limitTab.limitScreen.LimitScreen
import com.billy.glintforce.mainApplication.limitTab.limitScreen.goalInputScreen.EditGoalScreen
import com.billy.glintforce.mainApplication.limitTab.limitScreen.goalInputScreen.GoalDetailsDestination
import com.billy.glintforce.mainApplication.settingsTab.MyAccountScreen
import com.billy.glintforce.mainApplication.settingsTab.SettingsScreen
import com.billy.glintforce.mainApplication.settingsTab.reportTab.ReportScreen
import com.billy.glintforce.mainApplication.settingsTab.systemTab.SystemScreen
import com.billy.glintforce.viewModel.authentication.AuthViewModel

/**
 * NavHost to control navigation of different pages when logged in
 */
@Composable
fun Navigation(
    // Values
    navController: NavHostController,
    startDestination: String,

    // Actions
    changeTheme: () -> Unit,

    // ViewModels
    authViewModel: AuthViewModel = viewModel(),
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Composable for main tabs
        composable(route = "home") {
            HomeScreen(
                navigateDisplay = { navController.navigate("displayGoals") },
                navigateAdd = { navController.navigate("addExpense") },
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(route = "database") {
            DatabaseScreen(
                navigateEdit = { navController.navigate("expenseEdit/${it}") },
                navigateManage = { navController.navigate("manageCategories") },
            )
        }
        composable(route = "limit") {
            LimitScreen(
                onClick = { navController.navigate("goalManage") },
                navigateEdit = { navController.navigate("goalEdit/${it}") }
            )
        }

        // Composable for Home Screen
        composable(route = "displayGoals") {
            DisplayGoalScreen(onNavigateUp = { navController.navigateUp() })
        }
        composable(route = "addExpense") {
            AddExpenseScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() },
            )
        }
        composable(route = "addFromWidget") {
            val activity = (LocalContext.current as? Activity)

            AddExpenseScreen(
                onNavigateUp = { navController.navigateUp() },
                canNavigateBack = false,
                navigateBack = { activity?.finish() },
            )
        }

        // Composable for Database Screen
        composable(
            route = "expenseEdit/{param}",
            arguments = listOf(navArgument("param") {
                type = NavType.StringType
            })
        ) {backStackEntry ->
            val param = backStackEntry.arguments?.getString("param")

            EditExpenseScreen(
                param = param ?: "",
                onNavigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(route = "manageCategories") {
            ManageCategoriesScreen(
                navigateAdd = { navController.navigate("insertCategory") },
                navigateEdit = { navController.navigate("categoryEdit/${it}") },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(route = "insertCategory") {
            InsertCategoryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = CategoryDetailsDestination.ROUTEWITHARGS,
            arguments = listOf(navArgument(CategoryDetailsDestination.CATEGORYIDARG) {
                type = NavType.IntType
            })
        ) {
            EditCategoryScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() }
            )
        }

        // Composable for Limit Screen
        composable(
            route = GoalDetailsDestination.ROUTEWITHARGS,
            arguments = listOf(navArgument(GoalDetailsDestination.GOALIDARG) {
                type = NavType.IntType
            })
        ) {
            EditGoalScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(route = "goalManage") {
            ManageTypeScreen(
                navigateAdd = { navController.navigate("goalEntry") },
                navigateEdit = { navController.navigate("goalTypeEdit/${it}") },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(route = "goalEntry") {
            InsertTypeScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = GoalTypeDetailsDestination.ROUTEWITHARGS,
            arguments = listOf(navArgument(GoalTypeDetailsDestination.GOALTYPEIDARG) {
                type = NavType.IntType
            })
        ) {
            EditTypeScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        // Composable for Settings
        composable(route = "myAccount") {
            MyAccountScreen(authViewModel = authViewModel)
        }
        composable(route = "settings") {
            SettingsScreen(
                onClick = { screen ->
                    if (screen != "") {
                        navController.navigate(screen)
                    }
                }
            )
        }
        composable(route = "report") {
            ReportScreen(
                onNavigateUp = { navController.navigateUp() },
                //getDirectoryClick = getDirectoryClick
            )
        }
        composable(route = "system") {
            SystemScreen(
                onNavigateUp = { navController.navigateUp() },
                changeTheme = changeTheme
            )
        }
    }
}