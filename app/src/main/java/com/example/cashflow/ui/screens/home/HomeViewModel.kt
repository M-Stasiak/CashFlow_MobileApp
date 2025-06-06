package com.example.cashflow.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.AppSessionRepository
import com.example.cashflow.data.local.model.TransactionCategory
import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.data.local.model.TransactionType
import com.example.cashflow.data.repository.TransactionRepository
import com.example.cashflow.ui.core.AppUiEvent
import com.example.cashflow.ui.core.CommonUiEvent
import com.example.cashflow.ui.screens.login.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val sessionRepository: AppSessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AppUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog = _showLogoutDialog.asStateFlow()

    private val user = sessionRepository.currentUser.value
        ?: throw IllegalStateException("User not logged in")

    init {
        viewModelScope.launch {
            transactionRepository.getTransactionsForUser(user).collect { transactions ->
                _uiState.update { it.copy(transactions = transactions) }
            }
        }

        viewModelScope.launch {
            transactionRepository.getAccountBalance(user).collect { balance ->
                _uiState.update { it.copy(balance = balance) }
            }
        }

        viewModelScope.launch {
            transactionRepository.getTotalIncome(user).collect { income ->
                _uiState.update { it.copy(income = income) }
            }
        }

        viewModelScope.launch {
            transactionRepository.getTotalExpense(user).collect { expense ->
                _uiState.update { it.copy(expense = expense) }
            }
        }
    }

    fun setShowLogoutDialog(show: Boolean) {
        _showLogoutDialog.value = show
    }

    fun onAddTransactionClick() {
        viewModelScope.launch {
            _uiEvent.emit(CommonUiEvent.NavigateToSaveTransaction(null))
        }
    }

    fun onEditTransactionClick(transactionId: Long) {
        viewModelScope.launch {
            _uiEvent.emit(CommonUiEvent.NavigateToSaveTransaction(transactionId))
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionRepository.clearCurrentUser()
            _uiEvent.emit(CommonUiEvent.NavigateToLogin)
        }
    }
}