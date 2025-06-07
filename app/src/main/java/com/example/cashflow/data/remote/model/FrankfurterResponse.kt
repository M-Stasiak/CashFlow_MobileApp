package com.example.cashflow.data.remote.model

data class FrankfurterResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)