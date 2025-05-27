package com.example.cashflow.ui.screens

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
import androidx.compose.material.icons.filled.VpnKey
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cashflow.navigation.NavRoute
import com.example.cashflow.ui.theme.CashFlowTheme
import com.example.cashflow.viewmodel.LoginStatus
import com.example.cashflow.viewmodel.RegisterStatus
import com.example.cashflow.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val name by viewModel.name.collectAsState()
    val login by viewModel.login.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val isPasswordVisible by viewModel.isPasswordVisible.collectAsState()
    val isConfirmPasswordVisible by viewModel.isConfirmPasswordVisible.collectAsState()
    val registerStatus by viewModel.registerStatus.collectAsState()

    LaunchedEffect(registerStatus) {
        if (registerStatus == RegisterStatus.Success) {
            navController.navigate(NavRoute.HomeScreen)
        }
    }
    
    RegisterScreenContent(
        modifier = Modifier.fillMaxSize(),
        name = name,
        login = login,
        password = password,
        confirmPassword = confirmPassword,
        isPasswordVisible = isPasswordVisible,
        isConfirmPasswordVisible = isConfirmPasswordVisible,
        registerStatus = registerStatus,
        onRegisterStatusChange = { viewModel.onRegisterStatusChange(it) },
        onNameChange = { viewModel.onNameChange(it) },
        onLoginChange = { viewModel.onLoginChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onConfirmPasswordChange = { viewModel.onConfirmPasswordChange(it) },
        togglePasswordVisibility = { viewModel.togglePasswordVisibility() },
        toggleConfirmPasswordVisibility = { viewModel.toggleConfirmPasswordVisibility() },
        onRegister = { viewModel.register() }
    )
}

@Composable
fun RegisterScreenContent(
    modifier: Modifier = Modifier,
    name: String,
    login: String,
    password: String,
    confirmPassword: String,
    isPasswordVisible: Boolean,
    isConfirmPasswordVisible: Boolean,
    registerStatus: RegisterStatus,
    onRegisterStatusChange: (RegisterStatus) -> Unit,
    onNameChange: (String) -> Unit,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    togglePasswordVisibility: () -> Unit,
    toggleConfirmPasswordVisibility: () -> Unit,
    onRegister: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
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
                value = name,
                onValueChange = { onNameChange(it) },
                label = { Text("Imię i nazwisko") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login
            OutlinedTextField(
                value = login,
                onValueChange = {
                    onLoginChange(it)
                    if (registerStatus != RegisterStatus.Idle) {
                        onRegisterStatusChange(RegisterStatus.Idle)
                    }
                },
                label = { Text("Login") },
                isError = registerStatus == RegisterStatus.LoginTaken,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hasło
            OutlinedTextField(
                value = password,
                onValueChange = { onPasswordChange(it) },
                label = { Text("Hasło") },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { togglePasswordVisibility() }) {
                        Icon(
                            if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
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
                value = confirmPassword,
                onValueChange = { onConfirmPasswordChange(it) },
                label = { Text("Powtórz hasło") },
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { toggleConfirmPasswordVisibility() }) {
                        Icon(
                            if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Pokaż/ukryj hasło"
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (registerStatus != RegisterStatus.Idle) {
                Text(
                    text = when (registerStatus) {
                        RegisterStatus.LoginTaken -> "Login jest już zajęty"
                        RegisterStatus.Error -> "Wystąpił błąd"
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
                onClick = { onRegister() },
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
            name = "Mati",
            login = "mati",
            password = "password",
            confirmPassword = "confirmPassword",
            isPasswordVisible = true,
            isConfirmPasswordVisible = true,
            registerStatus = RegisterStatus.Idle,
            onRegisterStatusChange = { },
            onNameChange = { },
            onLoginChange = { },
            onPasswordChange = { },
            onConfirmPasswordChange = { },
            togglePasswordVisibility = { },
            toggleConfirmPasswordVisibility = { },
            onRegister = { }
        )
    }
}