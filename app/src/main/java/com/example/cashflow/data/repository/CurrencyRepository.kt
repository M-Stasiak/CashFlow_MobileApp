package com.example.cashflow.data.repository

import com.example.cashflow.data.remote.NetworkModule
import com.example.cashflow.data.remote.api.FrankfurterApi
import com.example.cashflow.data.remote.model.FrankfurterResponse
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val api: FrankfurterApi
) {

    suspend fun getLatestRates(base: String): FrankfurterResponse {
        return api.getLatestRates(base)
    }

    suspend fun getCurrencyNames(): Map<String, String> {
        return api.getCurrencyNames()
    }
}