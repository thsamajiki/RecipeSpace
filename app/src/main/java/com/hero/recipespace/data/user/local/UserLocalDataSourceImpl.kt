package com.hero.recipespace.data.user.local

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.listener.OnCompleteListener

class UserLocalDataSourceImpl : UserLocalDataSource {
    override fun getData(userKey: String, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        userKey: String,
        onCompleteListener: OnCompleteListener<List<UserData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun clear() {
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