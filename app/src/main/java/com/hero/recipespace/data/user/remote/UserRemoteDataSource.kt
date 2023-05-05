package com.hero.recipespace.data.user.remote

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.domain.user.request.LoginUserRequest
import com.hero.recipespace.domain.user.request.SignUpUserRequest
import com.hero.recipespace.domain.user.request.UpdateUserRequest

interface UserRemoteDataSource {
    suspend fun login(request: LoginUserRequest) : UserData

    suspend fun getUserData(userKey: String) : UserData

    suspend fun getFirebaseAuthProfile(): UserData

    suspend fun getDataList() : List<UserData>

    suspend fun add(request: SignUpUserRequest) : UserData

    suspend fun updateUser(request: UpdateUserRequest) : UserData

    suspend fun remove(userData: UserData) : UserData

    suspend fun signOut()
}