package com.example.cashflow.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.AppSessionRepository
import com.example.cashflow.data.local.model.TransactionCategory
import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.data.local.model.TransactionType
import com.example.cashflow.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val sessionRepository: AppSessionRepository
) : ViewModel() {

    private val user = sessionRepository.currentUser.value
        ?: throw IllegalStateException("User not logged in")
    val transactions = transactionRepository.getTransactionsForUser(user)
    val balance = transactionRepository.getAccountBalance(user)
    val income = transactionRepository.getTotalIncome(user)
    val expense = transactionRepository.getTotalExpense(user)
}