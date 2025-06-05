package com.example.cashflow.ui.screens.home

import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.ui.core.AppUiEvent

sealed interface HomeUiEvent : AppUiEvent {
    data class ShowMessage(val message: String) : HomeUiEvent
}

data class HomeUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val balance: Float = 0f,
    val income: Float = 0f,
    val expense: Float = 0f
)

data class HomeUiCallbacks(
    val onAddTransactionClick: () -> Unit,
    val onEditTransactionClick: (TransactionEntity) -> Unit
)