package com.billy.glintforce.logIn

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.viewModel.authentication.AuthState
import com.billy.glintforce.viewModel.authentication.AuthViewModel
import com.billy.glintforce.viewModel.authentication.LoginViewModel

@Composable
fun ForgotPasswordScreen (
    color: Color,

    navigateLogIn: () -> Unit,

    viewModel: LoginViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val authState by authViewModel.authState.observeAsState()
    val context = LocalContext.current

    // Different actions depending on authentication state
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.PasswordReset -> {
                Toast.makeText(
                    context,
                    "Password reset email has been sent",
                    Toast.LENGTH_SHORT
                ).show()
                navigateLogIn()
            }
            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }
    }

    AuthScreenContent(
        textFieldModifier = Modifier.onKeyEvent {
            if (it.key == Key.Enter) {
                authViewModel.sendPasswordResetEmail(uiState.email, false)
            }
            false
        },

        color = color,
        heading = "Forgot Password",

        email = uiState.email,
        onValueChange = { viewModel.updateEmail(email = it) },

        buttonText = "Reset Password",
        onClick = { authViewModel.sendPasswordResetEmail(uiState.email, false) },

        navigationText = "Back to Login",
        navigationOnClick = { navigateLogIn() }
    )
}