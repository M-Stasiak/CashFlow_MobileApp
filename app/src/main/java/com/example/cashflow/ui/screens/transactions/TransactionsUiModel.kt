package com.example.cashflow.ui.screens.transactions

import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.data.local.model.TransactionType
import com.example.cashflow.ui.core.AppUiEvent

sealed interface TransactionsnUiEvent : AppUiEvent {
    data class ShowMessage(val message: String) : TransactionsnUiEvent
}

data class TransactionsUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val selectedFilter: TransactionType? = null
)

data class TransactionsUiCallbacks(
    val onFilterSelected: (TransactionType?) -> Unit,
    val onAddTransactionClick: () -> Unit,
    val onEditTransactionClick: (TransactionEntity) -> Unit
)