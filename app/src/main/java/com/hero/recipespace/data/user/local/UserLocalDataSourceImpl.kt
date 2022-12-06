package com.hero.recipespace.data.user.local

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.user.datastore.UserLocalStore
import kotlinx.coroutines.flow.Flow

class UserLocalDataSourceImpl(
    private val userLocalStore: UserLocalStore
) : UserLocalDataSource {
    override fun getData(
        userKey: String
    ): Flow<UserData> {
        return userLocalStore.getData(userKey)
    }

    override fun getDataList(): Flow<List<UserData>> {
        return userLocalStore.getDataList()
    }

    override fun clear() {
    }

    override suspend fun add(userData: UserData) {
        userLocalStore.add(userData)
    }

    override suspend fun update(userData: UserData) {
        userLocalStore.update(userData)
    }

    override suspend fun remove(
        userData: UserData
    ) {
        userLocalStore.remove(userData)
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }
}