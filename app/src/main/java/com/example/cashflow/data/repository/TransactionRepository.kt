package com.example.cashflow.data.repository

import com.example.cashflow.data.local.dao.TransactionDao
import com.example.cashflow.data.local.model.TransactionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val dao: TransactionDao
) {
    fun getLocalTransactions(): Flow<List<TransactionEntity>> = dao.getAllTransactions()

    suspend fun addTransaction(transactionEntity: TransactionEntity) = dao.insertTransaction(transactionEntity)
}