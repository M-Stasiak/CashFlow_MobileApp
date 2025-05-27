package com.example.cashflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cashflow.data.local.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY dateMillis DESC")
    fun getTransactionsForUser(userId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getTransactionById(transactionId: Long): TransactionEntity?

    @Query("""
        SELECT 
            IFNULL(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END), 0) -
            IFNULL(SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END), 0)
        FROM transactions
        WHERE userId = :userId
    """)
    fun getAccountBalance(userId: Long): Flow<Float>

    @Query("""
        SELECT IFNULL(SUM(amount), 0)
        FROM transactions
        WHERE userId = :userId AND type = 'INCOME'
    """)
    fun getTotalIncome(userId: Long): Flow<Float>

    @Query("""
        SELECT IFNULL(SUM(amount), 0)
        FROM transactions
        WHERE userId = :userId AND type = 'EXPENSE'
    """)
    fun getTotalExpense(userId: Long): Flow<Float>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
}