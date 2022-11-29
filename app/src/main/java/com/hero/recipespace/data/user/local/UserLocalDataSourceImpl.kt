package com.hero.recipespace.data.user.local

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.user.datastore.UserLocalStore
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class UserLocalDataSourceImpl(
    private val userLocalStore: UserLocalStore
) : UserLocalDataSource {
    override suspend fun getData(
        userKey: String,
        onCompleteListener: OnCompleteListener<UserData>
    ) {
        userLocalStore.getData(userKey, object : OnCompleteListener<UserData> {
            override fun onComplete(isSuccess: Boolean, response: Response<UserData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override fun getDataList(
        onCompleteListener: OnCompleteListener<List<UserData>>
    ) {
        userLocalStore.getDataList(object : OnCompleteListener<List<UserData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<UserData>>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override fun clear() {
    }

    override suspend fun add(userData: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        userLocalStore.add(userData, object : OnCompleteListener<UserData> {
            override fun onComplete(isSuccess: Boolean, response: Response<UserData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override suspend fun update(
        userData: UserData,
        onCompleteListener: OnCompleteListener<UserData>,
    ) {
        userLocalStore.update(userData, object : OnCompleteListener<UserData> {
            override fun onComplete(isSuccess: Boolean, response: Response<UserData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override suspend fun remove(
        userData: UserData,
        onCompleteListener: OnCompleteListener<UserData>,
    ) {
        userLocalStore.remove(userData, object : OnCompleteListener<UserData> {
            override fun onComplete(isSuccess: Boolean, response: Response<UserData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }
}