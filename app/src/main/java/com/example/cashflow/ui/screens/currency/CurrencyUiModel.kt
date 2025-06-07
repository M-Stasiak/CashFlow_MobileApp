package com.example.cashflow.ui.screens.currency

data class CurrencyUiState(
    val base: String = "EUR",
    val rates: Map<String, Double> = emptyMap(),
    val currencyNames: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDropdownExpanded: Boolean = false,
)

data class CurrencyUiCallbacks(
    val onBaseCurrencyChange: (String) -> Unit,
    val onDropdownToggle: () -> Unit
)
