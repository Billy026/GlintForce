package com.billy.glintforce.viewModel.authentication

/**
 * Class containing all data needed for login
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val confirmPassword: String = "",
    val isConfirmPasswordVisible: Boolean = false
)