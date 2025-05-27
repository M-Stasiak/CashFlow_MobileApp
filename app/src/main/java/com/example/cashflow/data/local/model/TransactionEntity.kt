package com.example.cashflow.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val category: TransactionCategory,
    val description: String,
    val amount: Float,
    val type: TransactionType,
    val dateMillis: Long
)

enum class TransactionType { INCOME, EXPENSE }

enum class TransactionCategory { GROCERIES, RENT, TRANSPORT, ENTERTAINMENT, SALARY, FREELANCE, TRANSFER }
