package com.example.cashflow.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.data.local.model.TransactionType
import com.example.cashflow.ui.components.getTransactionCategoryUiByEnum
import com.example.cashflow.util.formatDate
import java.util.Locale

@Composable
fun TransactionList(
    modifier: Modifier = Modifier,
    transactions: List<TransactionEntity>
) {
    LazyColumn(modifier = modifier.padding(horizontal = 0.dp)) {
        items(transactions) { transaction ->
            val transactionCategoryUi = getTransactionCategoryUiByEnum(transaction.category)
            TransactionItem(
                modifier = Modifier,
                categoryName = transactionCategoryUi.title,
                amount = transaction.amount,
                type = transaction.type,
                categoryIcon = transactionCategoryUi.icon,
                date = formatDate(transaction.dateMillis)
            )
        }
    }
}

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    categoryName: String,
    amount: Float,
    type: TransactionType,
    categoryIcon: ImageVector,
    date: String,
) {
    val iconColor = when (type) {
        TransactionType.INCOME -> Color(0xFF4CAF50) // zielony
        TransactionType.EXPENSE -> Color(0xFFF44336) // czerwony
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = categoryIcon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(32.dp)
                    .background(iconColor.copy(alpha = 0.1f), shape = CircleShape)
                    .padding(6.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = (if (type == TransactionType.INCOME) "+ " else "- ") + String.format(Locale.US, "%.2f", amount) + " zł",
            style = MaterialTheme.typography.bodyLarge,
            color = iconColor,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}