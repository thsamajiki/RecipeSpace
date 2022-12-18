package com.hero.recipespace.domain.user.repository

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.request.LoginUserRequest
import com.hero.recipespace.domain.user.request.SignUpUserRequest
import com.hero.recipespace.domain.user.request.UpdateUserRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(request: LoginUserRequest): UserEntity

    suspend fun getAccountProfile(): UserEntity

    fun observeUserList(): Flow<List<UserEntity>>

    suspend fun addUser(request: SignUpUserRequest)

    suspend fun updateUser(userEntity: UserEntity)

    // 레시피를 업로드하는 것과 유사하게 함수를 짜야할 수도 있어서 만들어놓음
    suspend fun updateUser(request: UpdateUserRequest, onProgress: (Float) -> Unit) : UserEntity

    suspend fun deleteUser(userEntity: UserEntity)

    suspend fun signOut()
}