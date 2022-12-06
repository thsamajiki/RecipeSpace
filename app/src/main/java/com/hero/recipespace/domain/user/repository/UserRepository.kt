package com.hero.recipespace.domain.user.repository

import com.hero.recipespace.domain.user.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(userKey: String): Flow<UserEntity>

    fun getAccountProfile(): Flow<UserEntity>

    fun getUserList()

    suspend fun addUser(
        userName: String,
        email: String,
        pwd: String
    )

    suspend fun updateUser(userEntity: UserEntity)

    suspend fun deleteUser(userEntity: UserEntity)

    suspend fun signOut()
}