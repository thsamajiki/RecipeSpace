package com.hero.recipespace.database.user

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.data.user.local.UserLocalDataSource
import com.hero.recipespace.data.user.remote.UserRemoteDataSource
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.mapper.toEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override fun getUser(userKey: String): Flow<UserEntity> {

    }

    override fun getAccountProfile(): UserEntity {
        return userRemoteDataSource.getFirebaseAuthProfile().toEntity()
    }

    override fun getUserList(): Flow<List<UserEntity>> {
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
    }

    override suspend fun updateUser(
        userEntity: UserEntity
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(
        userEntity: UserEntity
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        userRemoteDataSource.signOut()
    }
}