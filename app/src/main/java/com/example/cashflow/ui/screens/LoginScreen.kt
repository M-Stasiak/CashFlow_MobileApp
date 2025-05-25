package com.example.cashflow.ui.screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cashflow.ui.theme.CashFlowTheme
import com.example.cashflow.viewmodel.LoginStatus
import com.example.cashflow.viewmodel.LoginViewModel
import com.example.cashflow.viewmodel.RegisterStatus

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    val isLoginFieldVisible by viewModel.isLoginFieldVisible.collectAsState()
    val login by viewModel.login.collectAsState()
    val password by viewModel.password.collectAsState()
    val isPasswordVisible by viewModel.isPasswordVisible.collectAsState()
    val loginStatus by viewModel.loginStatus.collectAsState()

    LaunchedEffect(loginStatus) {
        when (loginStatus) {
            LoginStatus.Success -> onNavigateToHome()
            LoginStatus.CreateAccount -> onNavigateToRegister()
            else -> { }
        }
    }

    LoginScreenContent(
        modifier = Modifier.fillMaxSize(),
        isLoginFieldVisible = isLoginFieldVisible,
        login = login,
        password = password,
        isPasswordVisible = isPasswordVisible,
        loginStatus = loginStatus,
        onLoginStatusChange = { viewModel.onLoginStatusChange(it) },
        onLoginChange = { viewModel.onLoginChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        togglePasswordVisibility = { viewModel.togglePasswordVisibility() },
        toggleLoginFieldVisibility = { viewModel.toggleLoginFieldVisibility() },
        onLogin = { viewModel.login() },
        onRegister = { viewModel.onLoginStatusChange(LoginStatus.CreateAccount) }
    )
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    isLoginFieldVisible: Boolean,
    login: String,
    password: String,
    isPasswordVisible: Boolean,
    loginStatus: LoginStatus,
    onLoginStatusChange: (LoginStatus) -> Unit,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    togglePasswordVisibility: () -> Unit,
    toggleLoginFieldVisibility: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit

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
                AnimatedVisibility(visible = isLoginFieldVisible) {
                    Column {
                        OutlinedTextField(
                            value = login,
                            onValueChange = { onLoginChange(it) },
                            label = { Text("Login") },
                            placeholder = { Text("Login") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Hasło
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        onPasswordChange(it)
                        if (loginStatus != LoginStatus.Idle) {
                            onLoginStatusChange(LoginStatus.Idle)
                        }
                    },
                    label = { Text("Hasło") },
                    placeholder = { Text("Hasło") },
                    isError = loginStatus == LoginStatus.WrongPassword,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { togglePasswordVisibility() }) {
                            Icon(
                                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
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
                    TextButton(onClick = { onRegister() }) {
                        Text("Utwórz konto")
                    }

                    TextButton(onClick = { toggleLoginFieldVisibility() }) {
                        Text(if (isLoginFieldVisible) "Anuluj zmianę" else "Zmień konto")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (loginStatus != LoginStatus.Idle) {
                    Text(
                        text = when (loginStatus) {
                            LoginStatus.WrongPassword -> "Błędne hasło"
                            LoginStatus.Error -> "Wystąpił błąd"
                            else -> ""
                        },
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { onLogin() },
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
            isLoginFieldVisible = true,
            login = "login",
            password = "password",
            isPasswordVisible = true,
            loginStatus = LoginStatus.WrongPassword,
            onLoginStatusChange = { },
            onLoginChange = { },
            onPasswordChange = { },
            togglePasswordVisibility = { },
            toggleLoginFieldVisibility = { },
            onLogin = { },
            onRegister = { }
        )
    }
}