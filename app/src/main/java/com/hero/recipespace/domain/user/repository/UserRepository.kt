package com.hero.recipespace.domain.user.repository

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.request.LoginUserRequest
import com.hero.recipespace.domain.user.request.SignUpUserRequest
import com.hero.recipespace.domain.user.request.UpdateUserRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(request: LoginUserRequest): UserEntity

    suspend fun getUser(userKey: String) : UserEntity

    suspend fun getAccountProfile(): UserEntity

    fun observeUserList(): Flow<List<UserEntity>>

    suspend fun addUser(request: SignUpUserRequest)

    suspend fun updateUser(request: UpdateUserRequest) : UserEntity

    suspend fun deleteUser(userEntity: UserEntity)

    suspend fun signOut()
}