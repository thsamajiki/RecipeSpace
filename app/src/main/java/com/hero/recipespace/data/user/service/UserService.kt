package com.hero.recipespace.data.user.service

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.domain.user.request.LoginUserRequest
import com.hero.recipespace.domain.user.request.SignUpUserRequest

interface UserService {
    suspend fun login(request: LoginUserRequest) : UserData

    fun getFirebaseAuthProfile(): UserData

    fun getDataList() : List<UserData>

    suspend fun add(request: SignUpUserRequest) : UserData

    suspend fun update(userData: UserData) : UserData

    suspend fun uploadImage(profileImageUrl: String, progress: (Float) -> Unit): String

    suspend fun remove(userData: UserData) : UserData

    suspend fun signOut()
}