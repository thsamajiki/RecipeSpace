package com.hero.recipespace.data.user.remote

import com.hero.recipespace.data.user.UserData
import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {
    fun getData(userKey: String) : Flow<UserData>

    fun getFirebaseAuthProfile(): UserData

    fun getDataList() : Flow<List<UserData>>

    suspend fun add(userData: UserData)

    suspend fun update(userData: UserData)

    suspend fun remove(userData: UserData)

    suspend fun signOut()
}