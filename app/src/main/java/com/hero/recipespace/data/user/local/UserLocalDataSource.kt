package com.hero.recipespace.data.user.local

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.OnCompleteListener

interface UserLocalDataSource {
    suspend fun getData(userKey: String, onCompleteListener: OnCompleteListener<UserData>)

    fun getDataList(onCompleteListener: OnCompleteListener<List<UserData>>)

    fun clear()

    suspend fun add(userData: UserData, onCompleteListener: OnCompleteListener<UserData>)

    suspend fun update(userData: UserData, onCompleteListener: OnCompleteListener<UserData>)

    suspend fun remove(userData: UserData, onCompleteListener: OnCompleteListener<UserData>)
}