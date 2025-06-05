package com.example.cashflow.ui.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.AppSessionRepository
import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.data.local.model.TransactionType
import com.example.cashflow.data.repository.TransactionRepository
import com.example.cashflow.ui.core.AppUiEvent
import com.example.cashflow.ui.core.CommonUiEvent
import com.example.cashflow.ui.screens.save_transaction.SaveTransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val sessionRepository: AppSessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AppUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val user = sessionRepository.currentUser.value
        ?: throw IllegalStateException("User not logged in")

    private val allTransactions = transactionRepository.getTransactionsForUser(user)

    init {
        viewModelScope.launch {
            combine(
                allTransactions,
                _uiState.map { it.selectedFilter }
            ) { transactions, filter ->
                filter?.let { type ->
                    transactions.filter { it.type == type }
                } ?: transactions
            }.collect { filtered ->
                _uiState.update { it.copy(transactions = filtered) }
            }
        }
    }

    fun onFilterSelected(type: TransactionType?) {
        _uiState.update {
            it.copy(selectedFilter = type)
        }
    }

    fun onAddTransactionClicked() {
        viewModelScope.launch {
            _uiEvent.emit(CommonUiEvent.NavigateToSaveTransaction(null))
        }
    }

    fun onEditTransactionClicked(transactionId: Long) {
        viewModelScope.launch {
            _uiEvent.emit(CommonUiEvent.NavigateToSaveTransaction(transactionId))
        }
    }
}