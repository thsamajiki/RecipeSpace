package com.hero.recipespace.data.user.service

import com.hero.recipespace.data.user.UserData

interface UserService {
    fun getData(userKey: String) : UserData

    fun getFirebaseAuthProfile(): UserData

    fun getDataList() : List<UserData>

    suspend fun add(userData: UserData)

    suspend fun update(userData: UserData)

    suspend fun remove(userData: UserData)

    suspend fun signOut()
}