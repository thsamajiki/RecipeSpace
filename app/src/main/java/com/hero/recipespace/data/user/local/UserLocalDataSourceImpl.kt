package com.hero.recipespace.data.user.local

import androidx.lifecycle.asFlow
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.user.dao.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
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

//    override suspend fun updateUserInfo(newUserName: String, newProfileImageUrl: String) {
//        userDao.updateUser(newUserName, newProfileImageUrl)
//    }

    override suspend fun remove(
        userData: UserData
    ) {
        userDao.deleteUser(userData)
    }

    override suspend fun signOut() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        userDao.deleteUser(userId)
    }

    override fun clear() {
    }
}