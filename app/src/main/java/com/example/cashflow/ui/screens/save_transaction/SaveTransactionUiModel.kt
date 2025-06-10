package com.example.cashflow.ui.screens.save_transaction

import android.graphics.Bitmap
import com.example.cashflow.data.local.model.TransactionCategory
import com.example.cashflow.data.local.model.TransactionType
import com.example.cashflow.ui.core.AppUiEvent

sealed interface SaveTransactionUiEvent : AppUiEvent {
    data class ShowMessage(val message: String) : SaveTransactionUiEvent
}

data class SaveTransactionUiState(
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val transactionCategory: TransactionCategory = TransactionCategory.TRANSFER,
    val amount: String = "",
    val description: String = "",
    val dateMillis: Long = 0L,
    val isDropdownExpanded: Boolean = false,
    val isNewTransaction: Boolean = true,
    val isEditing: Boolean = false
)

data class SaveTransactionUiCallbacks(
    val onTransactionTypeChanged: (TransactionType) -> Unit,
    val onTransactionCategoryChanged: (TransactionCategory) -> Unit,
    val onAmountChanged: (String) -> Unit,
    val onDescriptionChanged: (String) -> Unit,
    val onDateSelected: (Long) -> Unit,
    val onDropdownToggle: () -> Unit,
    val onReceiptAnalyze: (Bitmap) -> Unit,
    val onSaveExpense: () -> Unit,
    val onToggleEdit: () -> Unit,
    val onBackClick: () -> Unit
)