package com.example.cashflow.ui.core

interface AppUiEvent

sealed interface CommonUiEvent : AppUiEvent {
    object LoggedOutByShake : CommonUiEvent
    object NavigateBack : CommonUiEvent
    object NavigateUp : CommonUiEvent
    object NavigateToHome : CommonUiEvent
    object NavigateToLogin : CommonUiEvent
    object NavigateToRegister : CommonUiEvent
    data class NavigateToSaveTransaction(val transactionId: Long?) : CommonUiEvent
    object NavigateToTransactions : CommonUiEvent
    data class ShowGenericError(val message: String) : CommonUiEvent
}