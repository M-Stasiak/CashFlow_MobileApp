package com.example.cashflow.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.AppSessionRepository
import com.example.cashflow.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: AppSessionRepository
) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _login = MutableStateFlow("")
    val login = _login.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible = _isPasswordVisible.asStateFlow()

    private val _isConfirmPasswordVisible = MutableStateFlow(false)
    val isConfirmPasswordVisible = _isConfirmPasswordVisible.asStateFlow()

    private val _registerStatus = MutableStateFlow(RegisterStatus.Idle)
    val registerStatus = _registerStatus.asStateFlow()

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun onLoginChange(newLogin: String) {
        _login.value = newLogin
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
    }

    fun onRegisterStatusChange(newRegisterStatus: RegisterStatus) {
        _registerStatus.value = newRegisterStatus
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun toggleConfirmPasswordVisibility() {
        _isConfirmPasswordVisible.value = !_isConfirmPasswordVisible.value
    }

    fun register() {
        viewModelScope.launch {
            val isUserCreated = userRepository.registerUser(name = _name.value, login = _login.value, password = _password.value)
            if (isUserCreated) {
                val user = userRepository.loginUser(login = _login.value, password = _password.value)
                if (user != null) {
                    sessionRepository.setCurrentUser(user)
                    _registerStatus.value = RegisterStatus.Success
                }
                else _registerStatus.value = RegisterStatus.Error
            }
            else _registerStatus.value = RegisterStatus.LoginTaken
        }
    }
}

enum class RegisterStatus { Idle, Success, LoginTaken, Error}