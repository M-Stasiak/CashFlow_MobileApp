package com.example.cashflow.data.repository

import com.example.cashflow.data.local.dao.TransactionDao
import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val dao: TransactionDao
) {
    fun getTransactionsForUser(userEntity: UserEntity): Flow<List<TransactionEntity>> = dao.getTransactionsForUser(userEntity.id)
    suspend fun getTransactionById(transactionId: Long): TransactionEntity? = dao.getTransactionById(transactionId)
    fun getTotalIncome(userEntity: UserEntity): Flow<Float> = dao.getTotalIncome(userEntity.id)
    fun getTotalExpense(userEntity: UserEntity): Flow<Float> = dao.getTotalExpense(userEntity.id)
    fun getAccountBalance(userEntity: UserEntity): Flow<Float> = dao.getAccountBalance(userEntity.id)

    suspend fun insertTransaction(transactionEntity: TransactionEntity) = dao.insertTransaction(transactionEntity)
    suspend fun updateTransaction(transactionEntity: TransactionEntity) = dao.updateTransaction(transactionEntity)
}