package com.example.cashflow.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.cashflow.data.local.model.TransactionCategory

data class TransactionCategoryUi(
    val category: TransactionCategory,
    val icon: ImageVector,
    val title: String
)

val allTransactionCategories = listOf(
    TransactionCategoryUi(TransactionCategory.GROCERIES, Icons.Default.ShoppingCart, "Zakupy spożywcze"),
    TransactionCategoryUi(TransactionCategory.RENT, Icons.Default.Home, "Czynsz"),
    TransactionCategoryUi(TransactionCategory.TRANSPORT, Icons.Default.DirectionsCar, "Transport"),
    TransactionCategoryUi(TransactionCategory.ENTERTAINMENT, Icons.Default.Star, "Rozrywka"),
    TransactionCategoryUi(TransactionCategory.SALARY, Icons.Default.AttachMoney, "Wynagrodzenie"),
    TransactionCategoryUi(TransactionCategory.FREELANCE, Icons.Default.Work, "Zlecenia"),
    TransactionCategoryUi(TransactionCategory.TRANSFER, Icons.Default.SwapHoriz, "Przelew")
)

fun getTransactionCategoryUiByEnum(category: TransactionCategory): TransactionCategoryUi {
    return allTransactionCategories.first { it.category == category }
}