package com.hero.recipespace.domain.user.repository

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.request.UpdateUserRequest
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

//    suspend fun updateUserInfo(newUserName: String = "", newProfileImageUrl: String = "")

    // 레시피를 업로드하는 것과 유사하게 함수를 짜야할 수도 있어서 만들어놓음
    suspend fun updateUser(request: UpdateUserRequest, onProgress: (Float) -> Unit) : UserEntity

    suspend fun deleteUser(userEntity: UserEntity)

    suspend fun signOut()
}