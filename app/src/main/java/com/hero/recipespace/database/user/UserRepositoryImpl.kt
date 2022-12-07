package com.hero.recipespace.database.user

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.data.user.local.UserLocalDataSource
import com.hero.recipespace.data.user.remote.UserRemoteDataSource
import com.hero.recipespace.domain.recipe.mapper.toData
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.mapper.toData
import com.hero.recipespace.domain.user.mapper.toEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserRepositoryImpl(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun getUser(userKey: String): UserEntity {
        return userLocalDataSource.getData(userKey).toEntity()
    }

    override suspend fun getAccountProfile(): UserEntity {
        return userRemoteDataSource.getFirebaseAuthProfile().toEntity()
    }

    override fun observeUserList(): Flow<List<UserEntity>> {
        CoroutineScope(Dispatchers.IO).launch {
            val userList = userRemoteDataSource.getDataList()
            userLocalDataSource.addAll(userList)
            cancel()
        }

        return userLocalDataSource.getDataList()
            .map { it ->
                it.map {
                    it.toEntity()
                }
            }
    }

    override suspend fun addUser(
        userName: String,
        email: String,
        pwd: String
    ) {
        var userData: UserData
        userData.userName = userName
        userRemoteDataSource.add(userName, email)

        CoroutineScope(Dispatchers.IO).launch {
            userRemoteDataSource.add(recipeEntity.toData())
            userLocalDataSource.add(recipeEntity.toData())
            cancel()
        }
    }

    override suspend fun updateUser(
        userEntity: UserEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            userRemoteDataSource.update(userEntity.toData())
            userLocalDataSource.update(userEntity.toData())
            cancel()
        }
    }

    override suspend fun deleteUser(
        userEntity: UserEntity
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            userRemoteDataSource.remove(userEntity.toData())
            userLocalDataSource.remove(userEntity.toData())
            cancel()
        }
    }

    override suspend fun signOut() {
        userRemoteDataSource.signOut()
    }

    private fun getEntities(data: List<UserData>): List<UserEntity> {
        val result = mutableListOf<UserEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}