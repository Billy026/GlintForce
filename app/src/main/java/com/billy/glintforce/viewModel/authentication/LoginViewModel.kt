package com.billy.glintforce.viewModel.authentication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Class containing the LoginUiState and required functions to update and utilise it
 */
class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update { currentState ->
            currentState.copy(email = email)
        }
    }

    fun updatePassword(password: String) {
        _uiState.update { currentState ->
            currentState.copy(password = password)
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update { currentState ->
            currentState.copy(isPasswordVisible = !currentState.isPasswordVisible)
        }
    }

    fun updateConfirmPassword(password: String) {
        _uiState.update { currentState ->
            currentState.copy(confirmPassword = password)
        }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { currentState ->
            currentState.copy(isConfirmPasswordVisible = !currentState.isConfirmPasswordVisible)
        }
    }
}