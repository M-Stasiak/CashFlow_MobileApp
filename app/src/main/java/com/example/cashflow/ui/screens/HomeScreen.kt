package com.example.cashflow.ui.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cashflow.viewmodel.HomeViewModel
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val balance by viewModel.balance.collectAsState()
    val income by viewModel.income.collectAsState()
    val expense by viewModel.expense.collectAsState()
    val animatedBalance by animateFloatAsState(
        targetValue = balance,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "BalanceAnimation"
    )
    val animatedIncome by animateFloatAsState(
        targetValue = income,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "IncomeAnimation"
    )
    val animatedExpense by animateFloatAsState(
        targetValue = expense,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "ExpenseAnimation"
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
            Text("Good Afternoon", color = Color.Gray)
            Text("CodeWithFK", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            CardItem(
                balance = animatedBalance,
                income = animatedIncome,
                expense = animatedExpense
            )

            Text("Recent Transactions", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            TransactionList()
        }
    }
}

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    balance: Float, income: Float, expense: Float
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF19766A)
        )
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White)
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "$${String.format(Locale.US,"%.2f", balance)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Income
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ArrowCircleDown,
                            contentDescription = "Income",
                            tint = Color.White,
                            modifier = Modifier.height(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Income",
                            color = Color.White,
                            modifier = Modifier.height(18.dp)
                        )
                    }
                    Text(
                        text = "$${String.format(Locale.US, "%.2f", income)}",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                }
                // Expense
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ArrowCircleUp,
                            contentDescription = "Expense",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Income",
                            color = Color.White,
                            modifier = Modifier.height(18.dp)
                        )
                    }
                    Text(
                        text = "$${String.format(Locale.US, "%.2f", expense)}",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionList(
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 0.dp)) {
        items(5) { index ->
            TransactionItem(
                modifier = Modifier,
                title = "Netflix",
                amount = index.toFloat(),
                icon = Icons.Filled.MonetizationOn,
                date = "21.05.2025"
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
            text = if (amount >= 0f) "+$${String.format(Locale.US, "%.2f", kotlin.math.abs(amount))}" else "-$${String.format(Locale.US, "%.2f", kotlin.math.abs(amount))}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = if (amount >= 0f) Color(0xFF4CAF50) else Color(0xFFFA6C61)
        )
    }
}