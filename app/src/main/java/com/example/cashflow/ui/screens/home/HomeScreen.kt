package com.example.cashflow.ui.screens.home

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.cashflow.ui.core.CommonUiEvent
import com.example.cashflow.ui.theme.CashFlowTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()
    val showLogoutDialog by viewModel.showLogoutDialog.collectAsState()

    BackHandler {
        viewModel.setShowLogoutDialog(true)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is CommonUiEvent.NavigateToSaveTransaction -> { navController.navigate(NavRoute.SaveTransactionScreen(event.transactionId)) }
                is CommonUiEvent.NavigateToLogin -> {
                    navController.navigate(NavRoute.LoginScreen) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    val animatedBalance by animateFloatAsState(
        targetValue = uiState.balance,
        animationSpec = tween(durationMillis = 1000, delayMillis = 500, easing = FastOutSlowInEasing),
        label = "BalanceAnimation"
    )
    val animatedIncome by animateFloatAsState(
        targetValue = uiState.income,
        animationSpec = tween(durationMillis = 1000, delayMillis = 500, easing = FastOutSlowInEasing),
        label = "IncomeAnimation"
    )
    val animatedExpense by animateFloatAsState(
        targetValue = uiState.expense,
        animationSpec = tween(durationMillis = 1000, delayMillis = 500, easing = FastOutSlowInEasing),
        label = "ExpenseAnimation"
    )

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowLogoutDialog(false) },
            title = { Text("Uwaga") },
            text = { Text("Czy na pewno chcesz się wylogować?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setShowLogoutDialog(false)
                    viewModel.logout()
                }) {
                    Text("Wyloguj")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setShowLogoutDialog(false) }) {
                    Text("Anuluj")
                }
            }
        )
    }


    HomeScreenContent(
        state = uiState,
        animatedBalance = animatedBalance,
        animatedIncome = animatedIncome,
        animatedExpense = animatedExpense,
        callbacks = HomeUiCallbacks(
            onAddTransactionClick = { viewModel.onAddTransactionClick() },
            onEditTransactionClick = { viewModel.onEditTransactionClick(it.id) }
        )
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    animatedBalance: Float,
    animatedIncome: Float,
    animatedExpense: Float,
    callbacks: HomeUiCallbacks
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
            Text("Dzień dobry", color = Color.Gray)
            Text("CashFlow", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            CardItem(
                balance = animatedBalance,
                income = animatedIncome,
                expense = animatedExpense
            )

            Text("Ostatnie transakcje", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            TransactionList(
                transactions = state.transactions,
                onItemClick = { callbacks.onEditTransactionClick(it)}
            )
        }

        FloatingActionButton(
            onClick = { callbacks.onAddTransactionClick() },
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
            state = HomeUiState(),
            animatedBalance = 100f,
            animatedIncome = 100f,
            animatedExpense = 100f,
            callbacks = HomeUiCallbacks(
                onAddTransactionClick = { },
                onEditTransactionClick = { }
            )
        )
    }
}