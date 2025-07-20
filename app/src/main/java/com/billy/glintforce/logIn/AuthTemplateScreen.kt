package com.billy.glintforce.logIn

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.billy.glintforce.R
import com.billy.glintforce.navHosts.AuthNavHost
import com.billy.glintforce.theme.loginHeading
import com.billy.glintforce.viewModel.authentication.AuthState
import com.billy.glintforce.viewModel.authentication.AuthViewModel

/**
 * Holds common values to be used by all authentication screens and their navHost.
 */
@Composable
fun AuthTemplateScreen(
    navigateHome: () -> Unit,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()
    val navController = rememberNavController()
    val context = LocalContext.current
    val color = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.primary
    }

    //authViewModel.signOut()

    // Navigates to Home page if user is already logged in
    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Authenticated -> authViewModel.transitionToHome(navigateHome)
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
            else -> Unit
        }
    }

    AuthNavHost(
        navController = navController,
        color = color,
        authViewModel = authViewModel
    )
}

/**
 * Common template for all authentication screens.
 *
 * @param otherContent other UI components specific to each screen.
 */
@Composable
fun AuthScreenContent(
    @SuppressLint("ModifierParameter")
    textFieldModifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,

    color: Color,
    heading: String,
    otherContent: @Composable () -> Unit = {},

    email: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,

    buttonText: String,
    onClick: () -> Unit,
    buttonEnabled: Boolean = true,

    navigationText: String,
    navigationOnClick: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = if (isSystemInDarkTheme()) {
                    painterResource(id = R.drawable.light_mode_login)
                } else {
                    painterResource(id = R.drawable.light_mode_login)
                },
                contentScale = ContentScale.FillBounds
            ),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Text(text = heading, style = loginHeading, fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = textFieldModifier,
            value = email,
            onValueChange = onValueChange,
            label = { Text(text = "Email", color = Color.Black) },
            singleLine = true,
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        otherContent()

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = buttonModifier,
            onClick = onClick,
            enabled = buttonEnabled,
            colors = ButtonDefaults.buttonColors(containerColor = color)
        ) {
            Text(text = buttonText, color = Color.White)
        }

        TextButton(onClick = navigationOnClick) {
            Text(
                text = navigationText,
                color = color
            )
        }
    }
}

/**
 * Template for password text field shared by 2 screens.
 *
 * @param otherContent other UI components specific to each screen.
 */
@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    password: String,
    isPasswordVisible: Boolean,
    imeAction: ImeAction,

    onValueChange: (String) -> Unit,
    togglePasswordVisibility: () -> Unit,
    otherContent: @Composable () -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = password,
        onValueChange = onValueChange,
        label = { Text(text = "Password", color = Color.Black)},
        singleLine = true,
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            val icon = if (isPasswordVisible) {
                R.drawable.auth_is_visible
            } else {
                R.drawable.auth_is_not_visible
            }
            val description = if (isPasswordVisible) "Hide password" else "Show password"

            IconButton(onClick = togglePasswordVisibility) {
                Icon(painter = painterResource(id = icon), contentDescription = description)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )

    otherContent()

    Spacer(modifier = Modifier.height(8.dp))
}