package com.example.cashflow.data.remote

import com.example.cashflow.data.remote.api.FrankfurterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val FRANKFURTER_BASE_URL = "https://api.frankfurter.dev/v1/"

    @Provides
    @Named("frankfurter")
    fun provideFrankfurterRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(FRANKFURTER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideFrankfurterApi(@Named("frankfurter") retrofit: Retrofit): FrankfurterApi =
        retrofit.create(FrankfurterApi::class.java)
}