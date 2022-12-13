package com.hero.recipespace.data.user.service

import com.hero.recipespace.data.user.UserData

interface UserService {
    fun getData(userKey: String) : UserData

    fun getFirebaseAuthProfile(): UserData

    fun getDataList() : List<UserData>

    suspend fun add(userName: String,
                    email: String,
                    pwd: String) : UserData

    suspend fun update(userData: UserData) : UserData

    suspend fun remove(userData: UserData) : UserData

    suspend fun signOut()
}