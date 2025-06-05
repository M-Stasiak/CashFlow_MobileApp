package com.example.cashflow.ui.screens.login

import com.example.cashflow.ui.core.AppUiEvent

sealed interface LoginUiEvent : AppUiEvent {
    data object LoggedSuccessfully : LoginUiEvent
    data object WrongPassword : LoginUiEvent
}


data class LoginUiState(
    val login: String = "",
    val password: String = "",
    val isLoginFieldVisible: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isLastLoggedUser: Boolean = false,
    val fieldErrors: Map<LoginErrorField, String> = emptyMap()
)

data class LoginUiCallbacks(
    val onLoginChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val toggleLoginFieldVisibility: () -> Unit,
    val togglePasswordVisibility: () -> Unit,
    val onLoginClick: () -> Unit,
    val onCreateNewAccountClick: () -> Unit,
)

enum class LoginErrorField {
    Login, Password, General
}