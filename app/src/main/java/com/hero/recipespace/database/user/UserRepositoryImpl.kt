package com.hero.recipespace.database.user

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.OnFailedListener

class UserRepositoryImpl : UserRepository {
    override fun getUser(userKey: String, onCompleteListener: OnCompleteListener<UserEntity>) {
        TODO("Not yet implemented")
    }

    override fun getAccountProfile(): UserEntity {
        TODO("Not yet implemented")
    }

    override fun addUser(
        userName: String,
        email: String,
        pwd: String,
        onCompleteListener: OnCompleteListener<UserEntity>,
        onFailedListener: OnFailedListener,
    ) {
        TODO("Not yet implemented")
    }

    override fun updateUser(
        userEntity: UserEntity,
        onCompleteListener: OnCompleteListener<UserEntity>,
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(
        userEntity: UserEntity,
        onCompleteListener: OnCompleteListener<UserEntity>,
    ) {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
}