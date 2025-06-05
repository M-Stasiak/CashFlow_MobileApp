package com.example.cashflow.ui.screens.register

import com.example.cashflow.ui.core.AppUiEvent

sealed interface RegisterUiEvent : AppUiEvent {
    data object RegisteredSuccessfully : RegisterUiEvent
    data object LoggedSuccessfully : RegisterUiEvent
}


data class RegisterUiState(
    val name: String = "",
    val login: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val fieldErrors: Map<RegisterErrorField, String> = emptyMap()
)

data class RegisterUiCallbacks(
    val onNameChange: (String) -> Unit,
    val onLoginChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val onConfirmPasswordChange: (String) -> Unit,
    val togglePasswordVisibility: () -> Unit,
    val toggleConfirmPasswordVisibility: () -> Unit,
    val onRegisterClick: () -> Unit
)

enum class RegisterErrorField {
    Name, Login, Password, ConfirmPassword, General
}