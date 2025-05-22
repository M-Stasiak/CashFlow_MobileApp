package com.example.cashflow.ui.screens.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Icon
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
import java.util.Locale

@Composable
fun TransactionList(
    modifier: Modifier = Modifier,
    transactions: List<TransactionEntity>
) {
    LazyColumn(modifier = modifier.padding(horizontal = 0.dp)) {
        items(transactions) { transaction ->
            TransactionItem(
                modifier = Modifier,
                title = transaction.title,
                amount = transaction.amount,
                icon = Icons.Filled.MonetizationOn,
                date = transaction.date
            )
        }
    }
}

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    title: String,
    amount: Float,
    icon: ImageVector,
    date: String,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(51.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = date, fontSize = 13.sp, color = Color.LightGray)
            }
        }
        Text(
            text = if (amount >= 0f) "+$${String.format(Locale.US, "%.2f", kotlin.math.abs(amount))}" else "-$${String.format(
                Locale.US, "%.2f", kotlin.math.abs(amount))}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = if (amount >= 0f) Color(0xFF4CAF50) else Color(0xFFFA6C61)
        )
    }
}