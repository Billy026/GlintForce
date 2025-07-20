package com.billy.glintforce.logIn

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.viewModel.authentication.AuthViewModel
import com.billy.glintforce.viewModel.authentication.LoginViewModel

/**
 * Composable for Sign Up page
 */
@Composable
fun SignUpScreen(
    color: Color,

    navigateLogIn: () -> Unit,

    viewModel: LoginViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    AuthScreenContent(
        color = color,
        heading = "Welcome to GlintForce",
        otherContent = { TwoPasswordAuthentication(
            password = uiState.password,
            confirmPassword = uiState.confirmPassword,
            isPasswordVisible = uiState.isPasswordVisible,
            isConfirmPasswordVisible = uiState.isConfirmPasswordVisible,
            context = context,

            onPasswordValueChange = { viewModel.updatePassword(password = it) },
            onConfirmPasswordValueChange = { viewModel.updateConfirmPassword(password = it) },
            togglePasswordVisibility = { viewModel.togglePasswordVisibility() },
            toggleConfirmPasswordVisibility = { viewModel.toggleConfirmPasswordVisibility() },
            signUp = { authViewModel.signup(uiState.email, uiState.password) }
        ) },

        email = uiState.email,
        onValueChange = { viewModel.updateEmail(email = it) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),

        buttonText = "Create account",
        onClick = {
            if (uiState.password == uiState.confirmPassword) {
                authViewModel.signup(uiState.email, uiState.password)
            } else {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        },

        navigationText = "Already have an account? Login",
        navigationOnClick = { navigateLogIn() }
    )
}

@Composable
fun TwoPasswordAuthentication(
    password: String,
    confirmPassword: String,
    isPasswordVisible: Boolean,
    isConfirmPasswordVisible: Boolean,
    context: Context,

    onPasswordValueChange: (String) -> Unit,
    onConfirmPasswordValueChange: (String) -> Unit,
    togglePasswordVisibility: () -> Unit,
    toggleConfirmPasswordVisibility: () -> Unit,
    signUp: () -> Unit
) {
    PasswordTextField(
        password = password,
        isPasswordVisible = isPasswordVisible,
        imeAction = ImeAction.Next,

        onValueChange = onPasswordValueChange,
        togglePasswordVisibility = togglePasswordVisibility,
        otherContent = { ConfirmPasswordTextField(
            password = password,
            confirmPassword = confirmPassword,
            isConfirmPasswordVisible = isConfirmPasswordVisible,
            context = context,

            signUp = signUp,
            onValueChange = onConfirmPasswordValueChange,
            toggleConfirmPasswordVisibility = toggleConfirmPasswordVisibility
        ) }
    )
}

@Composable
fun ConfirmPasswordTextField(
    password: String,
    confirmPassword: String,
    isConfirmPasswordVisible: Boolean,
    context: Context,

    signUp: () -> Unit,
    onValueChange: (String) -> Unit,
    toggleConfirmPasswordVisibility: () -> Unit
) {
    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        modifier = Modifier.onKeyEvent {
            if (it.key == Key.Enter) {
                if (password == confirmPassword) {
                    signUp()
                } else {
                    Toast.makeText(
                        context,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            false
        },
        value = confirmPassword,
        onValueChange = onValueChange,
        label = { Text(text = "Confirm password", color = Color.Black)},
        visualTransformation = if (isConfirmPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            val icon = if (isConfirmPasswordVisible) {
                R.drawable.auth_is_visible
            } else {
                R.drawable.auth_is_not_visible
            }
            val description = if (isConfirmPasswordVisible) {
                "Hide password"
            } else {
                "Show password"
            }

            IconButton(onClick = { toggleConfirmPasswordVisibility() }) {
                Icon(painter = painterResource(id = icon), contentDescription = description)
            }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )
}