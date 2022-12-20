package com.hero.recipespace.data.user.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.data.user.service.UserService
import com.hero.recipespace.domain.user.request.LoginUserRequest
import com.hero.recipespace.domain.user.request.SignUpUserRequest
import com.hero.recipespace.domain.user.request.UpdateUserRequest
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserRemoteDataSource {

    override suspend fun login(request: LoginUserRequest): UserData {
        return userService.login(request)
    }

    override suspend fun getUserData(userKey: String): UserData {
        return userService.getUserData(userKey)
    }

    override suspend fun getFirebaseAuthProfile(): UserData {
        val firebaseUser: FirebaseUser? = getCurrentUser()

        val profileImageUrl: String? = if (firebaseUser!!.photoUrl != null) {
            firebaseUser.photoUrl.toString()
        } else {
            null
        }

        return UserData(
            key = firebaseUser.uid,
            name = firebaseUser.displayName,
            email = firebaseUser.email,
            profileImageUrl = profileImageUrl
        )
    }

    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override suspend fun getDataList(): List<UserData> {
        return userService.getDataList()
    }

    override suspend fun add(request: SignUpUserRequest)  : UserData {
        return userService.add(request)
    }

    override suspend fun updateUser(
        request: UpdateUserRequest
    ): UserData {
        val newUserName: String = request.newUserName
        val userKey: String = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        val newProfileImageUrl: String = request.newProfileImageUrl

        val newUserData = UserData(
            key = userKey,
            name = newUserName,
            email = FirebaseAuth.getInstance().currentUser?.email.orEmpty(),
            profileImageUrl = newProfileImageUrl
        )

        return userService.update(newUserData)
    }

    override suspend fun remove(
        userData: UserData
    ) : UserData {
        return userService.remove(userData)
    }

    override suspend fun signOut() {
        return FirebaseAuth.getInstance().signOut()
    }
}