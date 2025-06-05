package com.example.cashflow.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.AppSessionRepository
import com.example.cashflow.data.repository.LoginResult
import com.example.cashflow.data.repository.UserRepository
import com.example.cashflow.ui.core.AppUiEvent
import com.example.cashflow.ui.core.CommonUiEvent
import com.example.cashflow.ui.screens.login.LoginErrorField
import com.example.cashflow.ui.screens.login.LoginUiEvent
import com.example.cashflow.ui.screens.login.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: AppSessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AppUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onNameChange(newName: String) {
        _uiState.update {
            it.copy(name = newName, fieldErrors = it.fieldErrors - RegisterErrorField.Name)
        }
    }

    fun onLoginChange(newLogin: String) {
        _uiState.update {
            it.copy(login = newLogin, fieldErrors = it.fieldErrors - RegisterErrorField.Login)
        }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update {
            it.copy(password = newPassword, fieldErrors = it.fieldErrors - RegisterErrorField.Password)
        }
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.update {
            it.copy(confirmPassword = newConfirmPassword, fieldErrors = it.fieldErrors - RegisterErrorField.ConfirmPassword)
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun register() {
        viewModelScope.launch {
            val state = _uiState.value
            val errors = mutableMapOf<RegisterErrorField, String>()

            if (state.name.isBlank()) errors[RegisterErrorField.Name] = "Imię i nazwisko jest wymagane"
            if (state.login.isBlank()) errors[RegisterErrorField.Login] = "Login jest wymagany"
            if (state.password.isBlank()) errors[RegisterErrorField.Password] = "Hasło jest wymagane"
            if (state.password != state.confirmPassword) errors[RegisterErrorField.ConfirmPassword] = "Hasła się różnią"

            if (errors.isNotEmpty()) {
                _uiState.update { it.copy(fieldErrors = errors) }
                return@launch
            }

            val isUserCreated = userRepository.registerUser(name = state.name, login = state.login, password = state.password)

            if (isUserCreated) {
                _uiEvent.emit(RegisterUiEvent.RegisteredSuccessfully)
                val result = userRepository.loginUser(login = state.login, password = state.password)
                when (result) {
                    is LoginResult.Success -> {
                        sessionRepository.setCurrentUser(result.user)
                        _uiState.update { it.copy(fieldErrors = emptyMap()) }
                        _uiEvent.emit(RegisterUiEvent.LoggedSuccessfully)
                    }
                    else -> {
                        _uiState.update {
                            it.copy(fieldErrors = mapOf(RegisterErrorField.General to "Wystąpił błąd logowania"))
                        }
                    }
                }
            } else {
                _uiState.update {
                    it.copy(fieldErrors = mapOf(RegisterErrorField.Login to "Login jest już zajęty"))
                }
            }
        }
    }
}