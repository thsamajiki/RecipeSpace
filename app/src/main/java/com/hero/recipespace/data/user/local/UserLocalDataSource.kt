package com.hero.recipespace.data.user.local

import com.hero.recipespace.data.user.UserData
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    suspend fun getData(userKey: String) : UserData

    fun getDataList() : Flow<List<UserData>>

    fun clear()

    suspend fun add(userData: UserData)

    suspend fun addAll(userList: List<UserData>)

    suspend fun update(userData: UserData)

    suspend fun remove(userData: UserData)

    suspend fun signOut()
}