package com.hero.recipespace.database.user.datastore

import android.content.Context
import androidx.lifecycle.LiveData
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.LocalStore
import com.hero.recipespace.database.user.dao.UserDao
import com.hero.recipespace.listener.OnCompleteListener
import kotlinx.coroutines.flow.Flow

class UserLocalStore(
    private val userDao: UserDao) {

    companion object {
        private lateinit var instance : UserLocalStore

        fun getInstance(userDao: UserDao) : UserLocalStore {
            return synchronized(this) {
                instance ?: UserLocalStore(userDao).also {
                    instance = it
                }
            }
        }
    }

    fun getData(userKey: String) : Flow<UserData> {

        val userData: UserData = userDao.getUserFromKey(userKey)!!
    }

    fun getDataList() : Flow<List<UserData>> {

        val userDataList: LiveData<List<UserData>> = userDao.getAllUsers()
    }

    fun add(data: UserData) {
        userDao.insertUser(data)
    }

    fun update(data: UserData) {
        userDao.updateUser(data)
    }

    fun remove(data: UserData) {
        userDao.deleteUser(data)
    }
}