package com.example.cashflow.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.cashflow.data.local.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppSessionRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val USER_ID_KEY = longPreferencesKey("current_user_id")
    }

    private val _lastLoggedUserId = MutableStateFlow<Long?>(null)
    val lastLoggedUserId = _lastLoggedUserId.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.data
                .map { it[USER_ID_KEY] }
                .distinctUntilChanged()
                .collect { id ->
                    _lastLoggedUserId.value = id
                }
        }
    }

    suspend fun setCurrentUser(user: UserEntity) {
        _currentUser.value = user
        dataStore.edit { prefs -> prefs[USER_ID_KEY] = user.id }
    }

    suspend fun clearCurrentUser() {
        _currentUser.value = null
    }
}