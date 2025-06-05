package com.example.cashflow.ui.screens.login

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cashflow.navigation.NavRoute
import com.example.cashflow.ui.core.CommonUiEvent
import com.example.cashflow.ui.theme.CashFlowTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is LoginUiEvent.LoggedSuccessfully -> { navController.navigate(NavRoute.HomeScreen) }
                is CommonUiEvent.NavigateToRegister -> { navController.navigate(NavRoute.RegisterScreen) }
            }
        }
    }

    LoginScreenContent(
        state = uiState,
        callbacks = LoginUiCallbacks(
            onLoginChange = { viewModel.onLoginChange(it) },
            onPasswordChange = { viewModel.onPasswordChange(it) },
            toggleLoginFieldVisibility = { viewModel.toggleLoginFieldVisibility() },
            togglePasswordVisibility = { viewModel.togglePasswordVisibility() },
            onLoginClick = { viewModel.onLoginClick() },
            onCreateNewAccountClick = { viewModel.onCreateNewAccountClick() }
        )
    )
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    state: LoginUiState,
    callbacks: LoginUiCallbacks

) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
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

                // Login
                AnimatedVisibility(visible = state.isLoginFieldVisible) {
                    Column {
                        OutlinedTextField(
                            value = state.login,
                            onValueChange = { callbacks.onLoginChange(it) },
                            label = { Text("Login") },
                            placeholder = { Text("Login") },
                            isError = state.fieldErrors.containsKey(LoginErrorField.Login),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Hasło
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { callbacks.onPasswordChange(it) },
                    label = { Text("Hasło") },
                    placeholder = { Text("Hasło") },
                    isError = state.fieldErrors.containsKey(LoginErrorField.Password),
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { callbacks.togglePasswordVisibility() }) {
                            Icon(
                                if (state.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Pokaż/ukryj hasło"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { callbacks.onCreateNewAccountClick() }) {
                        Text("Utwórz konto")
                    }
                    if (state.isLastLoggedUser) {
                        TextButton(onClick = { callbacks.toggleLoginFieldVisibility() }) {
                            Text(if (state.isLoginFieldVisible) "Anuluj zmianę" else "Zmień konto")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LoginErrorField.entries.forEach { field ->
                    state.fieldErrors[field]?.let { error ->
                        Text(
                            text = error,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { callbacks.onLoginClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Zaloguj się")
                }

                Spacer(modifier = Modifier.height(16.dp))
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

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    CashFlowTheme {
        LoginScreenContent(
            state = LoginUiState(),
            callbacks = LoginUiCallbacks(
                onLoginChange = { },
                onPasswordChange = { },
                togglePasswordVisibility = { },
                toggleLoginFieldVisibility = { },
                onLoginClick = { },
                onCreateNewAccountClick = { }
            )
        )
    }
}