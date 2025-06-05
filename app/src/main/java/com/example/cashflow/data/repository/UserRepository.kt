package com.example.cashflow.data.repository

import com.example.cashflow.data.local.dao.UserDao
import com.example.cashflow.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: UserDao
) {
    fun getAllUsers(): Flow<List<UserEntity>> = dao.getAllUsers()

    suspend fun getUserByLogin(login: String): UserEntity? = dao.getUserByLogin(login)

    suspend fun getUserLoginById(id: Long): String? = dao.getUserLoginById(id)

    suspend fun registerUser(name: String, login: String, password: String): Boolean {
        val existing = dao.getUserByLogin(login)
        if (existing != null) return false

        val user = UserEntity(
            name = name,
            login = login,
            password = hashPassword(password)
        )
        dao.insertUser(user)
        return true
    }

    suspend fun loginUser(login: String, password: String): LoginResult {
        val user = dao.authenticate(login, hashPassword(password))
        if (user != null) return LoginResult.Success(user)

        val userExists = dao.getUserByLogin(login) != null
        return if (userExists) LoginResult.WrongPassword else LoginResult.UserNotFound
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

sealed class LoginResult {
    data class Success(val user: UserEntity) : LoginResult()
    data object WrongPassword : LoginResult()
    data object UserNotFound : LoginResult()
}