package com.example.cashflow.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.cashflow.data.local.model.TransactionCategory

data class TransactionCategoryUi(
    val category: TransactionCategory,
    val icon: ImageVector,
    val title: String
)

val allTransactionCategories = listOf(
    TransactionCategoryUi(TransactionCategory.GROCERIES, Icons.Default.Home, "GROCERIES"),
    TransactionCategoryUi(TransactionCategory.RENT, Icons.Default.Person, "RENT"),
    TransactionCategoryUi(TransactionCategory.TRANSPORT, Icons.Default.ShoppingCart, "TRANSPORT"),
    TransactionCategoryUi(TransactionCategory.ENTERTAINMENT, Icons.Default.Settings, "ENTERTAINMENT"),
    TransactionCategoryUi(TransactionCategory.SALARY, Icons.Default.Call, "SALARY"),
    TransactionCategoryUi(TransactionCategory.FREELANCE, Icons.Default.Email, "FREELANCE"),
    TransactionCategoryUi(TransactionCategory.TRANSFER, Icons.Default.Email, "TRANSFER")
)

fun getTransactionCategoryUiByEnum(category: TransactionCategory): TransactionCategoryUi {
    return allTransactionCategories.first { it.category == category }
}