package com.hero.recipespace.data.user.remote

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.OnCompleteListener

class UserRemoteDataSourceImpl : UserRemoteDataSource{
    override fun getData(userKey: String, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        userKey: String,
        onCompleteListener: OnCompleteListener<List<UserData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(userData: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }

    override fun update(userData: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }

    override fun remove(userData: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }

}