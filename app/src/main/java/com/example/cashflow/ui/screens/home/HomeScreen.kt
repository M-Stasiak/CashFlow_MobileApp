package com.example.cashflow.ui.screens.home

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.ui.screens.home.components.CardItem
import com.example.cashflow.ui.screens.home.components.TransactionList
import com.example.cashflow.ui.theme.CashFlowTheme
import com.example.cashflow.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val transactions by viewModel.transactions.collectAsState(initial = emptyList())
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

    HomeScreenContent(
        modifier = Modifier.fillMaxSize(),
        animatedBalance = animatedBalance,
        animatedIncome = animatedIncome,
        animatedExpense = animatedExpense,
        transactions = transactions,
        onClickButton = { viewModel.addTran() }
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    animatedBalance: Float,
    animatedIncome: Float,
    animatedExpense: Float,
    transactions: List<TransactionEntity>,
    onClickButton: () -> Unit
) {
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
            Button(
                onClick = onClickButton
            ) {
                Text("Siema")
            }

            Text("Recent Transactions", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            TransactionList(transactions = transactions)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CashFlowTheme {
        HomeScreenContent(
            animatedBalance = 100f,
            animatedIncome = 100f,
            animatedExpense = 100f,
            transactions = listOf(
                TransactionEntity(id = 1, userId = 0, title = "Groceries", description = "Opis", amount = 45.99f, date = "2025-05-20"),
                TransactionEntity(id = 2, userId = 0, title = "Netflix", description = "Opis", amount = 29.99f, date = "2025-05-18"),
                TransactionEntity(id = 3, userId = 0, title = "Salary", description = "Opis", amount = 5000.00f, date = "2025-05-15"),
                TransactionEntity(id = 4, userId = 0, title = "Coffee", description = "Opis", amount = 12.50f, date = "2025-05-21")
            ),
            onClickButton = { }
        )
    }
}