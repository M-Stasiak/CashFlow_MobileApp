package com.example.cashflow.ui.screens.save_transaction

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cashflow.data.local.model.TransactionCategory
import com.example.cashflow.data.local.model.TransactionType
import com.example.cashflow.navigation.NavRoute
import com.example.cashflow.ui.components.CameraButton
import com.example.cashflow.ui.components.getTransactionCategoryUiByEnum
import com.example.cashflow.ui.core.CommonUiEvent
import com.example.cashflow.util.formatDate
import java.util.*

@Composable
fun SaveTransactionScreen(
    modifier: Modifier = Modifier,
    viewModel: SaveTransactionViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is CommonUiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
                is CommonUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SaveTransactionScreenContent(
        state = uiState,
        callbacks = SaveTransactionUiCallbacks(
            onTransactionTypeChanged = { viewModel.onTransactionTypeChanged(it) },
            onTransactionCategoryChanged = { viewModel.onTransactionCategoryChanged(it) },
            onAmountChanged = { viewModel.onAmountChanged(it) },
            onDescriptionChanged = { viewModel.onDescriptionChanged(it) },
            onDateSelected = { viewModel.onDateSelected(it) },
            onDropdownToggle = { viewModel.toggleDropdown() },
            onReceiptAnalyze = { viewModel.analyzeReceipt(it) },
            onSaveExpense = { viewModel.saveTransaction() },
            onToggleEdit = { viewModel.onToggleEdit() },
            onBackClick = { viewModel.onBackClick() }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveTransactionScreenContent(
    modifier: Modifier = Modifier,
    state: SaveTransactionUiState,
    callbacks: SaveTransactionUiCallbacks
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
                callbacks.onDateSelected(pickedCalendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }


    val isFieldsEnabled = state.isNewTransaction or state.isEditing

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
                IconButton(
                    onClick = { callbacks.onBackClick() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Cofnij",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                if (!state.isNewTransaction) {
                    IconButton(
                        onClick = { callbacks.onToggleEdit() },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = if (state.isEditing) Icons.Default.Edit else Icons.Default.EditOff,
                            contentDescription = "Edytuj",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }

                val title = when {
                    state.isNewTransaction -> "Dodaj transakcję"
                    state.isEditing -> "Edytuj transakcję"
                    else -> "Szczegóły transakcji"
                }

                AnimatedContent(targetState = title, label = "TitleChange") { animatedTitle ->
                    Text(
                        text = animatedTitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
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
                            val selected = state.transactionType == type
                            OutlinedButton(
                                onClick = { if(isFieldsEnabled) callbacks.onTransactionTypeChanged(type) },
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
                        expanded = state.isDropdownExpanded,
                        onExpandedChange = { if (isFieldsEnabled) callbacks.onDropdownToggle() }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true).fillMaxWidth(),
                                value = getTransactionCategoryUiByEnum(state.transactionCategory).title,
                                onValueChange = { },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isDropdownExpanded)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = getTransactionCategoryUiByEnum(state.transactionCategory).icon,
                                        contentDescription = getTransactionCategoryUiByEnum(state.transactionCategory).title
                                    )
                                }
                            )
                        }
                        ExposedDropdownMenu(
                            expanded = state.isDropdownExpanded,
                            onDismissRequest = { callbacks.onDropdownToggle() },
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
                                    onClick = { callbacks.onTransactionCategoryChanged(item) },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Amount
                    TextField(
                        value = state.amount,
                        onValueChange = { callbacks.onAmountChanged(it) },
                        readOnly = !isFieldsEnabled,
                        label = { Text("Amount") },
                        placeholder = { Text("$") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    TextField(
                        value = state.description,
                        onValueChange = { callbacks.onDescriptionChanged(it) },
                        readOnly = !isFieldsEnabled,
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
                            .clickable { if (isFieldsEnabled) datePickerDialog.show() }
                    ) {
                        TextField(
                            value = formatDate(state.dateMillis),
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
                    AnimatedVisibility(visible = isFieldsEnabled) {
                        Column {
                            CameraButton(
                                modifier = Modifier.fillMaxWidth(),
                                onImageCaptured = { bitmap ->
                                    callbacks.onReceiptAnalyze(bitmap)
                                }
                            )
                            Button(
                                onClick = { callbacks.onSaveExpense() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Zapisz transakcję")
                            }
                        }
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
        state = SaveTransactionUiState(),
        callbacks = SaveTransactionUiCallbacks(
            onTransactionTypeChanged = { },
            onTransactionCategoryChanged = { },
            onAmountChanged = { },
            onDescriptionChanged = { },
            onDateSelected = { },
            onDropdownToggle = { },
            onReceiptAnalyze = { },
            onSaveExpense = { },
            onToggleEdit = { },
            onBackClick = { }
        )
    )
}
