package com.example.cashflow.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.AppSessionRepository
import com.example.cashflow.data.repository.LoginResult
import com.example.cashflow.data.repository.UserRepository
import com.example.cashflow.ui.core.AppUiEvent
import com.example.cashflow.ui.core.CommonUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: AppSessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AppUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var userLogin = ""

    fun onLoginChange(login: String) {
        _uiState.update {
            it.copy(
                login = login,
                fieldErrors = it.fieldErrors - LoginErrorField.Login
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                fieldErrors = it.fieldErrors - LoginErrorField.Password
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleLoginFieldVisibility() {
        _uiState.update { it.copy(isLoginFieldVisible = !it.isLoginFieldVisible) }
    }

    fun onLoginClick() {
        login()
    }

    fun onCreateNewAccountClick() {
        viewModelScope.launch {
            _uiEvent.emit(CommonUiEvent.NavigateToRegister)
            //_loginStatus.value = LoginStatus.CreateAccount
        }
    }

    fun login() {
        viewModelScope.launch {
            val currentState = _uiState.value
            var login = currentState.login

            if (currentState.isLastLoggedUser and !currentState.isLoginFieldVisible) login = userLogin
            val result = userRepository.loginUser(login = login, password = currentState.password)

            when (result) {
                is LoginResult.Success -> {
                    sessionRepository.setCurrentUser(result.user)
                    _uiState.update { it.copy(fieldErrors = emptyMap()) }
                    _uiEvent.emit(LoginUiEvent.LoggedSuccessfully)
                }
                is LoginResult.WrongPassword -> {
                    _uiState.update {
                        it.copy(fieldErrors = mapOf(LoginErrorField.Password to "Błędne hasło"))
                    }
                }
                is LoginResult.UserNotFound -> {
                    _uiState.update {
                        it.copy(fieldErrors = mapOf(LoginErrorField.Login to "Użytkownik nie istnieje"))
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            sessionRepository.lastLoggedUserId
                .filterNotNull()
                .firstOrNull()?.let { id ->
                    val state = _uiState.value
                    val userLoginPom = userRepository.getUserLoginById(id)
                    if (userLoginPom != null) {
                        userLogin = userLoginPom
                        _uiState.update { it.copy(isLastLoggedUser = true) }
                        return@launch
                    }
                }
            _uiState.update { it.copy(isLoginFieldVisible = true) }
        }
    }
}