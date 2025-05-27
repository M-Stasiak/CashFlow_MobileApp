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
    data class TransactionScreen(val transactionId: Long?) : NavRoute()
}