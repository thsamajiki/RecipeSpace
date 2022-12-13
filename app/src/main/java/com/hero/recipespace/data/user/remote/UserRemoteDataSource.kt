package com.hero.recipespace.data.user.remote

import com.hero.recipespace.data.user.UserData

interface UserRemoteDataSource {
    suspend fun getData(userKey: String) : UserData

    suspend fun getFirebaseAuthProfile(): UserData

    suspend fun getDataList() : List<UserData>

    suspend fun add(userName: String, email: String, pwd: String) : UserData

    suspend fun update(userData: UserData) : UserData

    suspend fun remove(userData: UserData) : UserData

    suspend fun signOut()
}