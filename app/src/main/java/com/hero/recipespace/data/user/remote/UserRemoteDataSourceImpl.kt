package com.hero.recipespace.data.user.remote

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.user.datastore.UserCloudStore
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response
import kotlinx.coroutines.flow.Flow

class UserRemoteDataSourceImpl(
    private val userCloudStore: UserCloudStore
) : UserRemoteDataSource {

    override fun getData(
        userKey: String
    ): Flow<UserData> {
        return userCloudStore.getData(userKey)
    }

    override fun getFirebaseAuthProfile(): Flow<UserData> {
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

    override fun getDataList(
    ): Flow<List<UserData>> {
        return userCloudStore.getDataList()
    }

    override suspend fun add(userData: UserData) {
        userCloudStore.add(userData)
    }

    override suspend fun update(
        userData: UserData
    ) {
        userCloudStore.update(userData)
    }

    override suspend fun remove(
        userData: UserData
    ) {
        userCloudStore.remove(userData)
    }

    override suspend fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}