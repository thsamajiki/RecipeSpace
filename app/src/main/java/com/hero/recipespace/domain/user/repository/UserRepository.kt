package com.hero.recipespace.domain.user.repository

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.OnFailedListener

interface UserRepository {
    fun getUser(userKey: String, onCompleteListener: OnCompleteListener<UserEntity>)

    fun getAccountProfile(): UserEntity

    fun addUser(
        userName: String,
        email: String,
        pwd: String,
        onCompleteListener: OnCompleteListener<UserEntity>,
        onFailedListener: OnFailedListener
    )

    fun updateUser(userEntity: UserEntity, onCompleteListener: OnCompleteListener<UserEntity>)

    fun deleteUser(userEntity: UserEntity, onCompleteListener: OnCompleteListener<UserEntity>)

    fun signOut()
}