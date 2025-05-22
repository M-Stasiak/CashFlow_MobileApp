package com.example.cashflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: TransactionRepository) : ViewModel() {

    private val _balance = MutableStateFlow(0f)
    val balance = _balance.asStateFlow()

    private val _income = MutableStateFlow(0f)
    val income = _income.asStateFlow()

    private val _expense = MutableStateFlow(0f)
    val expense = _expense.asStateFlow()

    fun updateBalance(newValue: Float) {
        _balance.value = newValue
    }

    fun addTran() {
        viewModelScope.launch {
            val transaction = TransactionEntity(
                id = null,
                title = "Losowa ${('A'..'Z').random()}",
                amount = (1..500).random().toFloat(),
                date = "siema"
            )
            repository.addTransaction(transaction)
        }
    }

    val transactions = repository.getLocalTransactions()

    private fun simulateBalanceChange() {
        viewModelScope.launch {
            delay(2000L)
            _balance.value = 123.45f
        }
    }

    private fun addRandomTransaction() {
        viewModelScope.launch {
            val transaction = TransactionEntity(
                id = null,
                title = "Losowa ${('A'..'Z').random()}",
                amount = (1..500).random().toFloat(),
                date = "siema"
            )
            repository.addTransaction(transaction)
        }
    }

    init {
        simulateBalanceChange()
        addRandomTransaction()
    }
}