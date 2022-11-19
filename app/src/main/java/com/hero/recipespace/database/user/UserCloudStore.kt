package com.hero.recipespace.database.user

import android.content.Context
import com.hero.recipespace.data.UserData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener

class UserCloudStore : CloudStore<UserData>() {
    companion object {
        private lateinit var instance : UserCloudStore

        fun getInstance(context: Context) : UserCloudStore {
            return instance ?: synchronized(this) {
                instance ?: UserCloudStore().also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any?, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any?,
        onCompleteListener: OnCompleteListener<List<UserData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(data: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }

    override fun update(data: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }

    override fun remove(data: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }
}