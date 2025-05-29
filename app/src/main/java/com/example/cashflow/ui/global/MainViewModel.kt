package com.example.cashflow.ui.global

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.AppSessionRepository
import com.example.cashflow.data.repository.UserRepository
import com.example.cashflow.navigation.NavRoute
import com.example.cashflow.ui.core.AppUiEvent
import com.example.cashflow.ui.core.CommonUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: AppSessionRepository
) : ViewModel() {

    private val _globalEvent = MutableSharedFlow<AppUiEvent>()
    val globalEvent = _globalEvent.asSharedFlow()

    private val _isBlackScreen = MutableStateFlow(false)
    val isBlackScreen = _isBlackScreen.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _startDestination = MutableStateFlow<NavRoute>(NavRoute.LoginScreen)
    val startDestination = _startDestination.asStateFlow()

    fun onShakeLogout() {
        viewModelScope.launch {
            _globalEvent.emit(CommonUiEvent.LoggedOutByShake)
            //_globalEvent.emit(CommonUiEvent.NavigateToLogin)
        }
    }

    fun setScreenBlack(value: Boolean) {
        _isBlackScreen.value = value
    }

    init {
        viewModelScope.launch {
            val users = userRepository.getAllUsers().first()
            if (users.isEmpty()) {
                _startDestination.value = NavRoute.RegisterScreen
            } else {
                _startDestination.value = NavRoute.LoginScreen
            }
            _isReady.value = true
        }
    }
}