package com.example.cashflow.data

import com.example.cashflow.data.local.model.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AppSessionRepository @Inject constructor() {

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser = _currentUser.asStateFlow()

    suspend fun setCurrentUser(user: UserEntity) {
        _currentUser.value = user
    }

    suspend fun clearCurrentUser() {
        _currentUser.value = null
    }
}