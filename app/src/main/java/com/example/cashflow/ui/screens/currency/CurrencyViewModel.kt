package com.example.cashflow.ui.screens.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val names = currencyRepository.getCurrencyNames()
                _uiState.update { it.copy(currencyNames = names) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
        loadRates()
    }

    private fun loadRates() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = currencyRepository.getLatestRates(_uiState.value.base)
                val invertedRates = response.rates.mapValues { 1 / it.value }
                _uiState.value = _uiState.value.copy(
                    rates = invertedRates,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Nieznany błąd"
                )
            }
        }
    }

    fun setBaseCurrency(base: String) {
        _uiState.value = _uiState.value.copy(base = base, isDropdownExpanded = false)
        loadRates()
    }

    fun onDropdownToggle() {
        _uiState.value = _uiState.value.copy(isDropdownExpanded = !_uiState.value.isDropdownExpanded)
    }
}
