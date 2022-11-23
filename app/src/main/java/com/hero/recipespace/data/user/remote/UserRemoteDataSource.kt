package com.hero.recipespace.data.user.remote

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.OnCompleteListener

interface UserRemoteDataSource {
    fun getData(userKey: String, onCompleteListener: OnCompleteListener<UserData>)

    fun getDataList(userKey: String, onCompleteListener: OnCompleteListener<List<UserData>>)

    fun add(userData: UserData, onCompleteListener: OnCompleteListener<UserData>)

    fun update(userData: UserData, onCompleteListener: OnCompleteListener<UserData>)

    fun remove(userData: UserData, onCompleteListener: OnCompleteListener<UserData>)
}