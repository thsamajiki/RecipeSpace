package com.hero.recipespace.data.user.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.data.user.service.UserService

class UserRemoteDataSourceImpl(
    private val userService: UserService
) : UserRemoteDataSource {

    override suspend fun getData(userKey: String): UserData {
        return userService.getData(userKey)
    }

    override suspend fun getFirebaseAuthProfile(): UserData {
        val firebaseUser: FirebaseUser? = getCurrentUser()

        val profileImageUrl: String? = if (firebaseUser!!.photoUrl != null) {
            firebaseUser.photoUrl.toString()
        } else {
            null
        }

        val userEntity = UserData(
            firebaseUser.displayName,
            firebaseUser.email,
            profileImageUrl
        )

        userEntity.copy(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())

        return userEntity
    }

    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override suspend fun getDataList(): List<UserData> {
        return userService.getDataList()
    }

    override suspend fun add(userData: UserData) {
        userService.add(userData)
    }

    override suspend fun update(
        userData: UserData
    ) {
        userService.update(userData)
    }

    override suspend fun remove(
        userData: UserData
    ) {
        userService.remove(userData)
    }

    override suspend fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}