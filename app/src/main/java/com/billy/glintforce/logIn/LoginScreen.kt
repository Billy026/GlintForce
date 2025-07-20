package com.billy.glintforce.logIn

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.viewModel.authentication.AuthState
import com.billy.glintforce.viewModel.authentication.AuthViewModel
import com.billy.glintforce.viewModel.authentication.LoginViewModel

/**
 * Composable for Login page
 */
@Composable
fun LoginScreen(
    color: Color,

    navigateForgot: () -> Unit,
    navigateSignUp: () -> Unit,

    viewModel: LoginViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val authState = authViewModel.authState.observeAsState()

    AuthScreenContent(
        buttonModifier = Modifier.width(200.dp),

        color = color,
        heading = "Login to GlintForce",
        otherContent = { PasswordAndTextButton(
            color = color,
            password = uiState.password,
            isPasswordVisible = uiState.isPasswordVisible,

            onValueChange = { viewModel.updatePassword(password = it) },
            togglePasswordVisibility = { viewModel.togglePasswordVisibility() },
            logIn = { authViewModel.login(uiState.email, uiState.password) },
            navigateForgot = navigateForgot
        ) },

        email = uiState.email,
        onValueChange = { viewModel.updateEmail(email = it) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),

        buttonText = "Login",
        onClick = { authViewModel.login(uiState.email, uiState.password) },
        buttonEnabled = authState.value != AuthState.Loading,

        navigationText = "Don't have an account? Sign Up",
        navigationOnClick = { navigateSignUp() }
    )
}

@Composable
fun PasswordAndTextButton(
    color: Color,
    password: String,
    isPasswordVisible: Boolean,

    onValueChange: (String) -> Unit,
    togglePasswordVisibility: () -> Unit,
    logIn: () -> Unit,
    navigateForgot: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Column {
            PasswordTextField(
                modifier = Modifier.onKeyEvent {
                    if (it.key == Key.Enter) {
                        logIn()
                    }
                    false
                },
                password = password,
                isPasswordVisible = isPasswordVisible,
                imeAction = ImeAction.Done,

                onValueChange = onValueChange,
                togglePasswordVisibility = togglePasswordVisibility,
                otherContent = {
                    TextButton(
                        onClick = { navigateForgot() },
                        modifier = Modifier
                            .align(Alignment.End)
                    ) {
                        Text(
                            text = "Forgot password?",
                            fontSize = 12.sp,
                            color = color
                        )
                    }
                }
            )
        }
    }
}