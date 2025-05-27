package com.example.cashflow.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.cashflow.data.AppSessionRepository
import com.example.cashflow.data.local.AppDatabase
import com.example.cashflow.data.local.dao.TransactionDao
import com.example.cashflow.data.local.dao.UserDao
import com.example.cashflow.data.repository.TransactionRepository
import com.example.cashflow.data.repository.UserRepository
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
    fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("cashflow_preferences")
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase) : UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideUserRepository(dao: UserDao) : UserRepository {
        return UserRepository(dao)
    }

    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase) : TransactionDao {
        return appDatabase.transactionDao()
    }

    @Provides
    fun provideTransactionRepository(dao: TransactionDao) : TransactionRepository {
        return TransactionRepository(dao)
    }

    @Provides
    @Singleton
    fun provideAppSessionRepository(dataStore: DataStore<Preferences>) : AppSessionRepository {
        return AppSessionRepository(dataStore)
    }
}