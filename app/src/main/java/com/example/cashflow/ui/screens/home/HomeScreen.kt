package com.example.cashflow.ui.screens.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cashflow.data.local.model.TransactionCategory
import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.data.local.model.TransactionType
import com.example.cashflow.navigation.NavRoute
import com.example.cashflow.ui.screens.home.components.CardItem
import com.example.cashflow.ui.components.TransactionList
import com.example.cashflow.ui.theme.CashFlowTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val transactions by viewModel.transactions.collectAsState(initial = emptyList())
    val balance by viewModel.balance.collectAsState(initial = 0f)
    val income by viewModel.income.collectAsState(initial = 0f)
    val expense by viewModel.expense.collectAsState(initial = 0f)
    val animatedBalance by animateFloatAsState(
        targetValue = balance,
        animationSpec = tween(durationMillis = 1000, delayMillis = 500, easing = FastOutSlowInEasing),
        label = "BalanceAnimation"
    )
    val animatedIncome by animateFloatAsState(
        targetValue = income,
        animationSpec = tween(durationMillis = 1000, delayMillis = 500, easing = FastOutSlowInEasing),
        label = "IncomeAnimation"
    )
    val animatedExpense by animateFloatAsState(
        targetValue = expense,
        animationSpec = tween(durationMillis = 1000, delayMillis = 500, easing = FastOutSlowInEasing),
        label = "ExpenseAnimation"
    )

    HomeScreenContent(
        modifier = Modifier.fillMaxSize(),
        animatedBalance = animatedBalance,
        animatedIncome = animatedIncome,
        animatedExpense = animatedExpense,
        transactions = transactions,
        onAddTransaction = { navController.navigate(NavRoute.SaveTransactionScreen(null)) },
        onEditTransaction = { navController.navigate(NavRoute.SaveTransactionScreen(it.id)) }
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    animatedBalance: Float,
    animatedIncome: Float,
    animatedExpense: Float,
    transactions: List<TransactionEntity>,
    onAddTransaction: () -> Unit,
    onEditTransaction: (transaction: TransactionEntity) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
            TransactionList(
                transactions = transactions,
                onItemClick = { onEditTransaction(it)}
            )
        }

        FloatingActionButton(
            onClick = { onAddTransaction() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Dodaj transakcję"
            )
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
                TransactionEntity(id = 1, userId = 0, category = TransactionCategory.TRANSFER, description = "Opis", amount = 45.99f, type = TransactionType.INCOME, dateMillis = System.currentTimeMillis()),
                TransactionEntity(id = 2, userId = 0, category = TransactionCategory.TRANSFER, description = "Opis", amount = 29.99f, type = TransactionType.EXPENSE, dateMillis = 2),
                TransactionEntity(id = 3, userId = 0, category = TransactionCategory.TRANSFER, description = "Opis", amount = 5000.00f, type = TransactionType.EXPENSE, dateMillis = System.currentTimeMillis()),
                TransactionEntity(id = 4, userId = 0, category = TransactionCategory.TRANSFER, description = "Opis", amount = 12.50f, type = TransactionType.INCOME, dateMillis = System.currentTimeMillis())
            ),
            onAddTransaction = { },
            onEditTransaction = { }
        )
    }
}