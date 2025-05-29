package com.example.cashflow.ui.screens.save_transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.cashflow.data.AppSessionRepository
import com.example.cashflow.data.local.model.TransactionCategory
import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.data.local.model.TransactionType
import com.example.cashflow.data.repository.TransactionRepository
import com.example.cashflow.navigation.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveTransactionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val transactionRepository: TransactionRepository,
    private val sessionRepository: AppSessionRepository
) : ViewModel() {

    private val args: NavRoute.SaveTransactionScreen = savedStateHandle.toRoute()

    init {
        if (args.transactionId != null) loadTransaction(args.transactionId)
    }

    private val user = sessionRepository.currentUser.value
        ?: throw IllegalStateException("User not logged in")

    private val _isTransactionSaved = MutableStateFlow(false)
    val isTransactionSaved: StateFlow<Boolean> = _isTransactionSaved

    private val _transactionType = MutableStateFlow(TransactionType.EXPENSE)
    val transactionType: StateFlow<TransactionType> = _transactionType

    private val _transactionCategory = MutableStateFlow(TransactionCategory.TRANSFER)
    val transactionCategory: StateFlow<TransactionCategory> = _transactionCategory

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _dateMillis = MutableStateFlow(0L)
    val dateMillis: StateFlow<Long> = _dateMillis

    private val _isDropdownExpanded = MutableStateFlow(false)
    val isDropdownExpanded: StateFlow<Boolean> = _isDropdownExpanded

    fun onTransactionTypeChanged(type: TransactionType) {
        _transactionType.value = type
    }

    fun onTransactionCategoryChanged(item: TransactionCategory) {
        _transactionCategory.value = item
        _isDropdownExpanded.value = false
    }

    fun onAmountChanged(newAmount: String) {
        if (newAmount.matches(Regex("^\\d*\\.?\\d*\$"))) {
            _amount.value = newAmount
        }
    }

    fun onDescriptionChanged(newDescription: String) {
        _description.value = newDescription
    }

    fun onDateSelected(dateMillis: Long) {
        _dateMillis.value = dateMillis
    }

    fun toggleDropdown() {
        _isDropdownExpanded.value = !_isDropdownExpanded.value
    }

    fun saveTransaction() {
        viewModelScope.launch {
            val transaction = TransactionEntity(
                userId = user.id,
                category = _transactionCategory.value,
                description = _description.value,
                amount = _amount.value.toFloat(),
                type = _transactionType.value,
                dateMillis = _dateMillis.value
            )
            transactionRepository.insertTransaction(transaction)
            _isTransactionSaved.value = true
        }
    }

    fun loadTransaction(transactionId: Long) {
        viewModelScope.launch {
            val transaction = transactionRepository.getTransactionById(transactionId)
            if (transaction != null) {
                _transactionType.value = transaction.type
                _transactionCategory.value = transaction.category
                _amount.value = transaction.amount.toString()
                _description.value = transaction.description
                _dateMillis.value = transaction.dateMillis
            }
        }
    }
}