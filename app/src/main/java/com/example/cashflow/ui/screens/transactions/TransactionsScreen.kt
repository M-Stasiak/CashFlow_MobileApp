package com.example.cashflow.ui.screens.transactions

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cashflow.data.local.model.TransactionCategory
import com.example.cashflow.data.local.model.TransactionEntity
import com.example.cashflow.data.local.model.TransactionType
import com.example.cashflow.navigation.NavRoute
import com.example.cashflow.ui.components.TransactionList
import com.example.cashflow.ui.core.CommonUiEvent
import com.example.cashflow.ui.theme.CashFlowTheme

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionsViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is CommonUiEvent.NavigateToSaveTransaction -> {
                    navController.navigate(NavRoute.SaveTransactionScreen(event.transactionId))
                }
            }
        }
    }

    TransactionsScreenContent(
        modifier = modifier,
        state = uiState,
        callbacks = TransactionsUiCallbacks(
            onFilterSelected = { viewModel.onFilterSelected(it) },
            onAddTransactionClick = { viewModel.onAddTransactionClicked() },
            onEditTransactionClick = { viewModel.onEditTransactionClicked(it.id) }
        )
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreenContent(
    modifier: Modifier = Modifier,
    state: TransactionsUiState,
    callbacks: TransactionsUiCallbacks
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Twoje transakcje",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            FilterChipsRow(
                selectedFilter = state.selectedFilter,
                onFilterSelected = { callbacks.onFilterSelected(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                TransactionList(
                    modifier = Modifier.padding(vertical = 8.dp),
                    transactions = state.transactions,
                    onItemClick = { callbacks.onEditTransactionClick(it) }
                )
            }
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

@Composable
fun FilterChipsRow(
    selectedFilter: TransactionType?,
    onFilterSelected: (TransactionType?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedFilter == null,
            onClick = { onFilterSelected(null) },
            label = { Text("Wszystkie") }
        )
        FilterChip(
            selected = selectedFilter == TransactionType.INCOME,
            onClick = { onFilterSelected(TransactionType.INCOME) },
            label = { Text("Przychody") }
        )
        FilterChip(
            selected = selectedFilter == TransactionType.EXPENSE,
            onClick = { onFilterSelected(TransactionType.EXPENSE) },
            label = { Text("Wydatki") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionsScreenPreview() {
    CashFlowTheme {
        TransactionsScreenContent(
            state = TransactionsUiState(),
            callbacks = TransactionsUiCallbacks(
                onFilterSelected = { },
                onAddTransactionClick = { },
                onEditTransactionClick = { }
            )
        )
    }
}