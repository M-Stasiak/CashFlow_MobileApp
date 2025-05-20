package com.example.cashflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class HomeViewModel : ViewModel() {

    private val _balance = MutableStateFlow(0f)
    val balance = _balance.asStateFlow()

    private val _income = MutableStateFlow(0f)
    val income = _income.asStateFlow()

    private val _expense = MutableStateFlow(0f)
    val expense = _expense.asStateFlow()

    fun updateBalance(newValue: Float) {
        _balance.value = newValue
    }

    private fun simulateBalanceChange() {
        viewModelScope.launch {
            delay(2000L)
            _balance.value = 123.45f
        }
    }

    init {
        simulateBalanceChange()
    }
}