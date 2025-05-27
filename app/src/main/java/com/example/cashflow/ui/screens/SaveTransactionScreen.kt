package com.example.cashflow.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cashflow.data.local.model.TransactionCategory
import com.example.cashflow.data.local.model.TransactionType
import com.example.cashflow.navigation.NavRoute
import com.example.cashflow.ui.components.getTransactionCategoryUiByEnum
import com.example.cashflow.util.formatDate
import com.example.cashflow.viewmodel.SaveTransactionViewModel
import java.util.*

@Composable
fun SaveTransactionScreen(
    modifier: Modifier = Modifier,
    viewModel: SaveTransactionViewModel = hiltViewModel(),
    navController: NavHostController,
) {

    val isTransactionSaved by viewModel.isTransactionSaved.collectAsState()
    val transactionType by viewModel.transactionType.collectAsState()
    val transactionCategory by viewModel.transactionCategory.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val description by viewModel.description.collectAsState()
    val dateMillis by viewModel.dateMillis.collectAsState()
    val isDropdownExpanded by viewModel.isDropdownExpanded.collectAsState()

    LaunchedEffect(isTransactionSaved) {
        if (isTransactionSaved) {
            navController.popBackStack()
            navController.navigate(NavRoute.HomeScreen)
        }
    }

    SaveTransactionScreenContent(
        transactionType = transactionType,
        transactionCategory = transactionCategory,
        amount = amount,
        description = description,
        dateMillis = dateMillis,
        isDropdownExpanded = isDropdownExpanded,
        onTransactionTypeChanged = { viewModel.onTransactionTypeChanged(it) },
        onTransactionCategoryChanged = { viewModel.onTransactionCategoryChanged(it) },
        onAmountChanged = { viewModel.onAmountChanged(it) },
        onDescriptionChanged = { viewModel.onDescriptionChanged(it) },
        onDateSelected = { viewModel.onDateSelected(it) },
        onDropdownToggle = { viewModel.toggleDropdown() },
        onAddExpense = { viewModel.saveTransaction() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveTransactionScreenContent(
    transactionType: TransactionType,
    transactionCategory: TransactionCategory,
    amount: String,
    description: String,
    dateMillis: Long,
    isDropdownExpanded: Boolean,
    onTransactionTypeChanged: (TransactionType) -> Unit,
    onTransactionCategoryChanged: (TransactionCategory) -> Unit,
    onAmountChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDateSelected: (Long) -> Unit,
    onDropdownToggle: () -> Unit,
    onAddExpense: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                val pickedCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onDateSelected(pickedCalendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(color = MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add Expense",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Card(
                modifier = Modifier
                    .offset(y = (-40).dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Transaction Type
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val types = listOf(
                            TransactionType.EXPENSE to "Expense",
                            TransactionType.INCOME to "Income"
                        )

                        types.forEach { (type, label) ->
                            val selected = transactionType == type
                            OutlinedButton(
                                onClick = { onTransactionTypeChanged(type) },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                            ) {
                                Text(label)
                            }
                        }
                    }

                    // Category
                    ExposedDropdownMenuBox(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = isDropdownExpanded,
                        onExpandedChange = { onDropdownToggle() }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true).fillMaxWidth(),
                                value = getTransactionCategoryUiByEnum(transactionCategory).title,
                                onValueChange = { },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = getTransactionCategoryUiByEnum(transactionCategory).icon,
                                        contentDescription = getTransactionCategoryUiByEnum(transactionCategory).title
                                    )
                                }
                            )
                        }
                        ExposedDropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { onDropdownToggle() }
                        ) {
                            TransactionCategory.entries.forEachIndexed { _, item ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = getTransactionCategoryUiByEnum(item).icon,
                                                contentDescription = getTransactionCategoryUiByEnum(item).title
                                            )
                                            Text(getTransactionCategoryUiByEnum(item).title)
                                        }
                                    },
                                    onClick = { onTransactionCategoryChanged(item) },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Amount
                    TextField(
                        value = amount,
                        onValueChange = onAmountChanged,
                        label = { Text("Amount") },
                        placeholder = { Text("$") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    TextField(
                        value = description,
                        onValueChange = onDescriptionChanged,
                        label = { Text("Description") },
                        placeholder = { Text("Add note..." ) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Date
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePickerDialog.show() }
                    ) {
                        TextField(
                            value = formatDate(dateMillis),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Date") },
                            placeholder = { Text("Select date") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Save Button
                    Button(
                        onClick = onAddExpense,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Expense")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SaveTransactionScreenPreview() {
    SaveTransactionScreenContent(
        transactionCategory = TransactionCategory.TRANSFER,
        amount = "120.50",
        description = "Opis",
        dateMillis = System.currentTimeMillis(),
        transactionType = TransactionType.EXPENSE,
        isDropdownExpanded = false,
        onTransactionCategoryChanged = {},
        onAmountChanged = {},
        onDescriptionChanged = {},
        onDateSelected = {},
        onTransactionTypeChanged = {},
        onDropdownToggle = {},
        onAddExpense = {}
    )
}