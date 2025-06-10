package com.example.cashflow.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cashflow.navigation.NavRoute
import com.example.cashflow.ui.core.CommonUiEvent
import com.example.cashflow.ui.screens.login.LoginErrorField
import com.example.cashflow.ui.theme.CashFlowTheme

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is RegisterUiEvent.RegisteredSuccessfully -> { }
                is RegisterUiEvent.LoggedSuccessfully -> { navController.navigate(NavRoute.HomeScreen) }
            }
        }
    }

    RegisterScreenContent(
        state = uiState,
        callbacks = RegisterUiCallbacks(
            onNameChange = { viewModel.onNameChange(it) },
            onLoginChange = { viewModel.onLoginChange(it) },
            onPasswordChange = { viewModel.onPasswordChange(it) },
            onConfirmPasswordChange = { viewModel.onConfirmPasswordChange(it) },
            togglePasswordVisibility = { viewModel.togglePasswordVisibility() },
            toggleConfirmPasswordVisibility = { viewModel.toggleConfirmPasswordVisibility() },
            onRegisterClick = { viewModel.register() },
        )
    )
}

@Composable
fun RegisterScreenContent(
    modifier: Modifier = Modifier,
    state: RegisterUiState,
    callbacks: RegisterUiCallbacks
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = null,
                tint = Color(0xFF005CBF),
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Utwórz konto",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nazwa
            OutlinedTextField(
                value = state.name,
                onValueChange = { callbacks.onNameChange(it) },
                label = { Text("Imię i nazwisko") },
                isError = state.fieldErrors.containsKey(RegisterErrorField.Name),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login
            OutlinedTextField(
                value = state.login,
                onValueChange = { callbacks.onLoginChange(it) },
                label = { Text("Login") },
                isError = state.fieldErrors.containsKey(RegisterErrorField.Login),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hasło
            OutlinedTextField(
                value = state.password,
                onValueChange = { callbacks.onPasswordChange(it) },
                label = { Text("Hasło") },
                isError = state.fieldErrors.containsKey(RegisterErrorField.Password),
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { callbacks.togglePasswordVisibility() }) {
                        Icon(
                            if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Pokaż/ukryj hasło"
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Powtórz hasło
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { callbacks.onConfirmPasswordChange(it) },
                label = { Text("Powtórz hasło") },
                isError = state.fieldErrors.containsKey(RegisterErrorField.ConfirmPassword),
                visualTransformation = if (state.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { callbacks.toggleConfirmPasswordVisibility() }) {
                        Icon(
                            if (state.isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Pokaż/ukryj hasło"
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            RegisterErrorField.entries.forEach { field ->
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
                onClick = { callbacks.onRegisterClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005CBF))
            ) {
                Text(text = "Utwórz konto", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    CashFlowTheme {
        RegisterScreenContent(
            state = RegisterUiState(),
            callbacks = RegisterUiCallbacks(
                onNameChange = {  },
                onLoginChange = { },
                onPasswordChange = { },
                onConfirmPasswordChange = { },
                togglePasswordVisibility = { },
                toggleConfirmPasswordVisibility = { },
                onRegisterClick = { },
            )
        )
    }
}