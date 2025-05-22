package com.example.cashflow.di

import android.content.Context
import com.example.cashflow.data.local.AppDatabase
import com.example.cashflow.data.local.dao.TransactionDao
import com.example.cashflow.data.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase) : TransactionDao {
        return appDatabase.transactionDao()
    }

    @Provides
    fun provideTransactionRepository(dao: TransactionDao) : TransactionRepository {
        return TransactionRepository(dao)
    }
}