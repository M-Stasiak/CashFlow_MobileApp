package com.example.cashflow.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.AppSessionRepository
import com.example.cashflow.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: AppSessionRepository
) : ViewModel() {

    private val _isLoginFieldVisible = MutableStateFlow(false)
    val isLoginFieldVisible = _isLoginFieldVisible.asStateFlow()

    private val _login = MutableStateFlow("")
    val login = _login.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible = _isPasswordVisible.asStateFlow()

    private val _loginStatus = MutableStateFlow(LoginStatus.Idle)
    val loginStatus = _loginStatus.asStateFlow()

    fun onLoginStatusChange(newLoginStatus: LoginStatus) {
        _loginStatus.value = newLoginStatus
    }

    fun onLoginChange(newLogin: String) {
        _login.value = newLogin
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun toggleLoginFieldVisibility() {
        _isLoginFieldVisible.value = !_isLoginFieldVisible.value
    }

    fun login() {
        viewModelScope.launch {
            val user = userRepository.loginUser(login = _login.value, password = _password.value)
            if (user != null) {
                sessionRepository.setCurrentUser(user)
                _loginStatus.value = LoginStatus.Success
            }
            else {
                _loginStatus.value = LoginStatus.WrongPassword
            }
        }
    }
}

enum class LoginStatus { Idle, Success, WrongPassword, CreateAccount, Error}