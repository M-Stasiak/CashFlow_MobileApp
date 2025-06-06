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
import com.example.cashflow.ui.core.AppUiEvent
import com.example.cashflow.ui.core.CommonUiEvent
import com.example.cashflow.ui.screens.register.RegisterUiState
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

    private val _uiState = MutableStateFlow(SaveTransactionUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AppUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val user = sessionRepository.currentUser.value
        ?: throw IllegalStateException("User not logged in")

    init {
        args.transactionId?.let { transactionId ->
            _uiState.update { it.copy(isNewTransaction = false) }
            loadTransaction(transactionId)
        }
    }

    fun onTransactionTypeChanged(type: TransactionType) {
        _uiState.update { it.copy(transactionType = type) }
    }

    fun onTransactionCategoryChanged(category: TransactionCategory) {
        _uiState.update {
            it.copy(transactionCategory = category, isDropdownExpanded = false)
        }
    }

    fun onAmountChanged(newAmount: String) {
        if (newAmount.matches(Regex("^\\d*\\.?\\d*\$"))) {
            _uiState.update { it.copy(amount = newAmount) }
        }
    }

    fun onDescriptionChanged(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun onDateSelected(dateMillis: Long) {
        _uiState.update { it.copy(dateMillis = dateMillis) }
    }

    fun toggleDropdown() {
        _uiState.update { it.copy(isDropdownExpanded = !it.isDropdownExpanded) }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _uiEvent.emit(CommonUiEvent.NavigateBack)
        }
    }

    fun onToggleEdit() {
        if (!_uiState.value.isNewTransaction) {
            _uiState.update { it.copy(isEditing = !it.isEditing) }
        }
    }

    fun saveTransaction() {
        viewModelScope.launch {
            val state = _uiState.value

            if (state.amount.isBlank() || state.amount.toFloatOrNull() == null) {
                _uiEvent.emit(SaveTransactionUiEvent.ShowMessage("Amount is invalid"))
                return@launch
            }

            val transaction = TransactionEntity(
                id = args.transactionId ?: 0,
                userId = user.id,
                category = state.transactionCategory,
                description = state.description,
                amount = state.amount.toFloat(),
                type = state.transactionType,
                dateMillis = state.dateMillis
            )
            if (state.isNewTransaction) transactionRepository.insertTransaction(transaction)
            else transactionRepository.updateTransaction(transaction)
            _uiEvent.emit(CommonUiEvent.NavigateBack)
        }
    }

    private fun loadTransaction(transactionId: Long) {
        viewModelScope.launch {
            val transaction = transactionRepository.getTransactionById(transactionId)
            transaction?.let {
                _uiState.update {
                    it.copy(
                        transactionType = transaction.type,
                        transactionCategory = transaction.category,
                        amount = transaction.amount.toString(),
                        description = transaction.description,
                        dateMillis = transaction.dateMillis
                    )
                }
            }
        }
    }
}