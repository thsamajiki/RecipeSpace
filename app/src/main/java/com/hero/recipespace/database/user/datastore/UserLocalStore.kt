package com.hero.recipespace.database.user.datastore

import android.content.Context
import androidx.lifecycle.LiveData
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.LocalStore
import com.hero.recipespace.database.user.dao.UserDao
import com.hero.recipespace.listener.OnCompleteListener

class UserLocalStore(
    private val context: Context,
    private val userDao: UserDao
) : LocalStore<UserData>(context) {

    companion object {
        private lateinit var instance : UserLocalStore

        fun getInstance(context: Context, userDao: UserDao) : UserLocalStore {
            return instance ?: synchronized(this) {
                instance ?: UserLocalStore(context, userDao).also {
                    instance = it
                }
            }
        }
    }

    override suspend fun getData(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<UserData>
    ) {
        if (params.isEmpty()) {
            onCompleteListener.onComplete(false, null)
            return
        }
        val userKey: String = params[0].toString()
        val userData: UserData = userDao.getUserFromKey(userKey)!!
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<UserData>>
    ) {
        if (params.isEmpty()) {
            onCompleteListener.onComplete(false, null)
            return
        }

        val userDataList: LiveData<List<UserData>> = userDao.getAllUsers()

        onCompleteListener.onComplete(true, userDataList)
    }

    override suspend fun add(data: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        userDao.insertUser(data)

        onCompleteListener.onComplete(true, data)
    }

    override suspend fun update(data: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        userDao.updateUser(data)

        onCompleteListener.onComplete(true, data)
    }

    override suspend fun remove(data: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        userDao.deleteUser(data)

        onCompleteListener.onComplete(true, data)
    }
}