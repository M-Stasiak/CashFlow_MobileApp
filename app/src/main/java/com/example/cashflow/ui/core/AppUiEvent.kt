package com.example.cashflow.ui.core

interface AppUiEvent

sealed interface CommonUiEvent : AppUiEvent {
    data object LoggedOutByShake : CommonUiEvent
    data object NavigateBack : CommonUiEvent
    data object NavigateUp : CommonUiEvent
    data object NavigateToHome : CommonUiEvent
    data object NavigateToLogin : CommonUiEvent
    data object NavigateToRegister : CommonUiEvent
    data class NavigateToSaveTransaction(val transactionId: Long?) : CommonUiEvent
    data object NavigateToTransactions : CommonUiEvent
    data class ShowGenericError(val message: String) : CommonUiEvent
}