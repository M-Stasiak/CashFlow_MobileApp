package com.example.cashflow.ui.screens.login

import com.example.cashflow.ui.core.AppUiEvent

sealed interface LoginUiEvent : AppUiEvent {
    object LoggedSuccessfully : LoginUiEvent
    object WrongPassword : LoginUiEvent
}