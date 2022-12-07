package com.hero.recipespace.data.user.local

import androidx.lifecycle.asFlow
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.user.dao.UserDao
import kotlinx.coroutines.flow.Flow

class UserLocalDataSourceImpl(
    private val userDao: UserDao
) : UserLocalDataSource {
    override suspend fun getData(userKey: String): UserData {
        return userDao.getUserFromKey(userKey) ?: error("not found UserData")
    }

    override fun getDataList(): Flow<List<UserData>> {
        return userDao.getAllUsers().asFlow()
    }

    override suspend fun add(userData: UserData) {
        userDao.insertUser(userData)
    }

    override suspend fun addAll(userList: List<UserData>) {
        userDao.insertAll(userList)
    }

    override suspend fun update(userData: UserData) {
        userDao.updateUser(userData)
    }

    override suspend fun remove(
        userData: UserData
    ) {
        userDao.deleteUser(userData)
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

    override fun clear() {
    }
}