package com.hero.recipespace.domain.user.repository

import com.hero.recipespace.domain.user.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(userKey: String): UserEntity

    suspend fun getAccountProfile(): UserEntity

    fun observeUserList(): Flow<List<UserEntity>>

    suspend fun addUser(
        userName: String,
        email: String,
        pwd: String
    )

    suspend fun updateUser(userEntity: UserEntity)

    suspend fun deleteUser(userEntity: UserEntity)

    suspend fun signOut()
}