package com.hero.recipespace.database.user

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.data.user.local.UserLocalDataSource
import com.hero.recipespace.data.user.remote.UserRemoteDataSource
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.OnFailedListener
import com.hero.recipespace.listener.Response

class UserRepositoryImpl(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun getUser(userKey: String, onCompleteListener: OnCompleteListener<UserEntity>) {
        userLocalDataSource.getData(userKey, object : OnCompleteListener<UserData> {
            override fun onComplete(isSuccess: Boolean, response: Response<UserData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    userRemoteDataSource.getData(userKey, object : OnCompleteListener<UserData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<UserData>?) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
        })
    }

    override fun getAccountProfile(): UserEntity {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(
        userName: String,
        email: String,
        pwd: String,
        onCompleteListener: OnCompleteListener<UserEntity>,
        onFailedListener: OnFailedListener,
    ) {
        var userData: UserData
        userData.userName = userName
        userRemoteDataSource.add(userName, email)
    }

    override suspend fun updateUser(
        userEntity: UserEntity,
        onCompleteListener: OnCompleteListener<UserEntity>,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(
        userEntity: UserEntity,
        onCompleteListener: OnCompleteListener<UserEntity>,
    ) {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
}