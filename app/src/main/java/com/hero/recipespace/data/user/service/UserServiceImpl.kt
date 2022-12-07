package com.hero.recipespace.data.user.service

import com.hero.recipespace.data.user.UserData
import javax.inject.Inject

class UserServiceImpl @Inject constructor() : UserService {
    override fun getData(userKey: String): UserData {
        TODO("Not yet implemented")
    }

    override fun getFirebaseAuthProfile(): UserData {
        TODO("Not yet implemented")
    }

    override fun getDataList(): List<UserData> {
        TODO("Not yet implemented")
    }

    override suspend fun add(userData: UserData) {
        TODO("Not yet implemented")
    }

    override suspend fun update(userData: UserData) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(userData: UserData) {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

}