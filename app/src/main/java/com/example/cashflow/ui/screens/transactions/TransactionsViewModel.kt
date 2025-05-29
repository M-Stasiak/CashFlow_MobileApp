package com.example.cashflow.ui.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.AppSessionRepository
import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.data.local.model.TransactionType
import com.example.cashflow.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val sessionRepository: AppSessionRepository
) : ViewModel() {

    private val user = sessionRepository.currentUser.value
        ?: throw IllegalStateException("User not logged in")
    private val transactions = transactionRepository.getTransactionsForUser(user)

    private val _selectedFilter = MutableStateFlow<TransactionType?>(null)
    val selectedFilter: StateFlow<TransactionType?> = _selectedFilter

    val filteredTransactions: StateFlow<List<TransactionEntity>> =
        combine(transactions, _selectedFilter) { transactions, filter ->
            filter?.let { tType ->
                transactions.filter { it.type == tType }
            } ?: transactions
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onFilterSelected(type: TransactionType?) {
        _selectedFilter.value = type
    }
}