package com.hero.recipespace.data.user.remote

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.domain.user.request.UpdateUserRequest

interface UserRemoteDataSource {
    suspend fun getData(userKey: String) : UserData

    suspend fun getFirebaseAuthProfile(): UserData

    suspend fun getDataList() : List<UserData>

    suspend fun add(userName: String, email: String, pwd: String) : UserData

    suspend fun update(userData: UserData) : UserData

//    suspend fun update(newUserName: String, newProfileImageUrl: String) : UserData

    // 레시피를 업로드하는 것과 유사하게 함수를 짜야할 수도 있어서 만들어놓음
    suspend fun updateUser(request: UpdateUserRequest, onProgress: (Float) -> Unit) : UserData

    suspend fun remove(userData: UserData) : UserData

    suspend fun signOut()
}