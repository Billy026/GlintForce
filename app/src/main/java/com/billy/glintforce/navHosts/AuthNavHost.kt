package com.billy.glintforce.navHosts

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.billy.glintforce.logIn.ForgotPasswordScreen
import com.billy.glintforce.logIn.LoginScreen
import com.billy.glintforce.logIn.SignUpScreen
import com.billy.glintforce.viewModel.authentication.AuthViewModel
import com.billy.glintforce.viewModel.authentication.LoginViewModel

/**
 * NavHost to control navigation to the different authentication screens.
 */
@Composable
fun AuthNavHost(
    navController: NavHostController,
    color: Color,
    authViewModel: AuthViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            LoginScreen(
                color = color,
                navigateForgot = { navController.navigate("forgotPassword") },
                navigateSignUp = { navController.navigate("signup") },
                authViewModel = authViewModel
            )
        }

        composable(route = "signup") {
            SignUpScreen(
                color = color,
                navigateLogIn = { navController.navigate("login") },
                authViewModel = authViewModel
            )
        }

        composable(route = "forgotPassword") {
            ForgotPasswordScreen(
                color = color,
                navigateLogIn = { navController.navigate("login") },
                authViewModel = authViewModel
            )
        }
    }
}