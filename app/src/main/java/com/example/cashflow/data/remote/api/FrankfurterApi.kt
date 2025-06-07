package com.example.cashflow.data.remote.api

import com.example.cashflow.data.remote.model.FrankfurterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FrankfurterApi {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String
    ): FrankfurterResponse

    @GET("currencies")
    suspend fun getCurrencyNames(): Map<String, String>
}