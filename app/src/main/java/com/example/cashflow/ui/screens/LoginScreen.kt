package com.example.cashflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cashflow.viewmodel.LoginStatus
import com.example.cashflow.viewmodel.LoginViewModel

@Preview(showBackground = true)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(),
    onNavigateToHome: () -> Unit = {}
) {
    val password = viewModel.password
    val isPasswordVisible = viewModel.isPasswordVisible
    val loginStatus by viewModel.loginStatus.collectAsState()

    LaunchedEffect(loginStatus) {
        if (loginStatus == LoginStatus.Success) {
            onNavigateToHome()
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Spacer(modifier = Modifier.height(32.dp))

                // Logo + Nazwa
                Text(
                    text = "CashFlow",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Dzień dobry!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Zaloguj się do aplikacji",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Hasło
                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Hasło") },
                    placeholder = { Text("Hasło") },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                            Icon(
                                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Pokaż/ukryj hasło"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(onClick = { }) {
                    Text("Nie pamiętasz hasła?")
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.login() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Zaloguj się")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (loginStatus != LoginStatus.Idle) {
                    Text(
                        text = if (loginStatus == LoginStatus.Success) "Zalogowano!" else "Błędne hasło",
                        color = if (loginStatus == LoginStatus.Success) Color.Green else Color.Red
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Mateusz Stasiak",
                    textAlign = TextAlign.Center
                )
                Text("Wersja 0.0.5")

            }
        }
    }
}