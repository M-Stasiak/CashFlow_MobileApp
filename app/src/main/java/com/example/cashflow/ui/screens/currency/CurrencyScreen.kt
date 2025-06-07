package com.example.cashflow.ui.screens.currency

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cashflow.ui.theme.CashFlowTheme

@Composable
fun CurrencyScreen(
    modifier: Modifier = Modifier,
    viewModel: CurrencyViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()

    val animatedRatesSize by animateFloatAsState(
        targetValue = uiState.rates.size.toFloat(),
        animationSpec = tween(durationMillis = 800),
        label = "RatesSizeAnimation"
    )

    CurrencyScreenContent(
        state = uiState,
        animatedRatesSize = animatedRatesSize,
        callbacks = CurrencyUiCallbacks(
            onBaseCurrencyChange = { viewModel.setBaseCurrency(it) },
            onDropdownToggle = { viewModel.onDropdownToggle() }
        )
    )
}

@Composable
fun CurrencyScreenContent(
    state: CurrencyUiState,
    animatedRatesSize: Float,
    callbacks: CurrencyUiCallbacks
) {
    val currencyOptions = listOf("EUR", "USD", "GBP", "CHF", "PLN")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Wybierz walutę bazową:",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            Box {
                Button(onClick = { callbacks.onDropdownToggle() }) {
                    Text(state.base)
                }
                DropdownMenu(
                    expanded = state.isDropdownExpanded,
                    onDismissRequest = { callbacks.onDropdownToggle() }
                ) {
                    currencyOptions.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text(currency) },
                            onClick = { callbacks.onBaseCurrencyChange(currency) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Kursy walut względem ${state.base}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (state.rates.isEmpty() && state.error == null) {
            Text("Ładowanie...", style = MaterialTheme.typography.bodyMedium)
        } else if (state.error != null) {
            Text("Błąd: ${state.error}", color = Color.Red, style = MaterialTheme.typography.bodyMedium)
        } else {
            state.rates.entries.sortedBy { it.key }.forEach { (currency, value) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currencyName = state.currencyNames[currency] ?: currency
                        Text(
                            text = "$currencyName ($currency)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "1 $currency = %.4f ${state.base}".format(value),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Łącznie ${animatedRatesSize.toInt()} walut",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyScreenPreview() {
    CashFlowTheme {
        CurrencyScreenContent(
            state = CurrencyUiState(),
            animatedRatesSize = 2f,
            callbacks = CurrencyUiCallbacks(
                onBaseCurrencyChange = { },
                onDropdownToggle = { }
            )
        )
    }
}
