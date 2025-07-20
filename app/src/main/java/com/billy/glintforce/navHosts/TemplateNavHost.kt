package com.billy.glintforce.navHosts

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.billy.glintforce.logIn.AuthTemplateScreen
import com.billy.glintforce.mainApplication.screenTemplate.MainScreen
import com.billy.glintforce.viewModel.authentication.AuthViewModel

/**
 * NavHost to switch between logIn portion and main portion of app.
 */
@Composable
fun TemplateNavHost(
    navController: NavHostController,
    startDestination: String = "login",
    isFromWidget: Boolean = false,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = "login") {
            AuthTemplateScreen(
                navigateHome = {}, //{ navController.navigate("main") },
                authViewModel = authViewModel
            )
        }

        composable(route = "main") {
            MainScreen(
                startDestination = if (isFromWidget) "addFromWidget" else "home",
                navigateLogIn = { navController.navigate("login") },
                authViewModel = authViewModel
            )
        }
    }
}