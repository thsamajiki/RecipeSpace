package com.hero.recipespace.data.user.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.user.datastore.UserCloudStore
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class UserRemoteDataSourceImpl(
    private val userCloudStore: UserCloudStore,
) : UserRemoteDataSource {

    override suspend fun getData(
        userKey: String,
        onCompleteListener: OnCompleteListener<UserData>,
    ) {
        userCloudStore.getData(userKey, object : OnCompleteListener<UserData> {
            override fun onComplete(isSuccess: Boolean, response: Response<UserData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override fun getFirebaseAuthProfile(): UserData {
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
        userEntity.userKey = firebaseUser.uid
        userEntity.userName = firebaseUser.displayName
        if (firebaseUser.photoUrl != null) {
            userEntity.profileImageUrl = firebaseUser.photoUrl.toString()
        } else {
            userEntity.profileImageUrl = null
        }

        return userEntity
    }

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun getDataList(
        onCompleteListener: OnCompleteListener<List<UserData>>,
    ) {
        userCloudStore.getDataList(object : OnCompleteListener<List<UserData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<UserData>>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override suspend fun add(userData: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        userCloudStore.add(userData, object : OnCompleteListener<UserData> {
            override fun onComplete(isSuccess: Boolean, response: Response<UserData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override suspend fun update(
        userData: UserData,
        onCompleteListener: OnCompleteListener<UserData>,
    ) {
        userCloudStore.update(userData, object : OnCompleteListener<UserData> {
            override fun onComplete(isSuccess: Boolean, response: Response<UserData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override suspend fun remove(
        userData: UserData,
        onCompleteListener: OnCompleteListener<UserData>,
    ) {
        userCloudStore.remove(userData, object : OnCompleteListener<UserData> {
            override fun onComplete(isSuccess: Boolean, response: Response<UserData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override suspend fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}