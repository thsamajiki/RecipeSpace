package com.hero.recipespace.data.user.local

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.OnCompleteListener

interface UserLocalDataSource {
    fun getData(userKey: String, onCompleteListener: OnCompleteListener<UserData>)

    fun getDataList(userKey: String, onCompleteListener: OnCompleteListener<List<UserData>>)

    fun clear()

    fun add(userData: UserData, onCompleteListener: OnCompleteListener<UserData>)

    fun update(userData: UserData, onCompleteListener: OnCompleteListener<UserData>)

    fun remove(userData: UserData, onCompleteListener: OnCompleteListener<UserData>)
}