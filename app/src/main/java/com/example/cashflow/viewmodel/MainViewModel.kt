package com.example.cashflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashflow.data.repository.UserRepository
import com.example.cashflow.navigation.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _startDestination = MutableStateFlow<NavRoute>(NavRoute.LoginScreen)
    val startDestination = _startDestination.asStateFlow()

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