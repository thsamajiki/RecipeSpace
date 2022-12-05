package com.hero.recipespace.data.user.local

import androidx.lifecycle.LiveData
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.OnCompleteListener

interface UserLocalDataSource {
    suspend fun getData(userKey: String, onCompleteListener: OnCompleteListener<UserData>) : LiveData<UserData>

    fun getDataList(onCompleteListener: OnCompleteListener<List<UserData>>) : LiveData<List<UserData>>

    fun clear()

    suspend fun add(userData: UserData, onCompleteListener: OnCompleteListener<UserData>)

    suspend fun update(userData: UserData, onCompleteListener: OnCompleteListener<UserData>)

    suspend fun remove(userData: UserData, onCompleteListener: OnCompleteListener<UserData>)

    suspend fun signOut()
}