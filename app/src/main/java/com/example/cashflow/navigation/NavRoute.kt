package com.example.cashflow.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {
    @Serializable
    data object LoginScreen : NavRoute()

    @Serializable
    data object RegisterScreen : NavRoute()

    @Serializable
    data object HomeScreen : NavRoute()

    @Serializable
    data object TransactionsScreen : NavRoute()

    @Serializable
    data class SaveTransactionScreen(val transactionId: Long?) : NavRoute()
}