package com.example.cashflow.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    var password by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    private val _loginStatus = MutableStateFlow(LoginStatus.Idle)
    val loginStatus = _loginStatus.asStateFlow()

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun login() {
        _loginStatus.value = if (password == "admin") LoginStatus.Success else LoginStatus.WrongPassword
    }
}

enum class LoginStatus { Idle, Success, WrongPassword, Error}