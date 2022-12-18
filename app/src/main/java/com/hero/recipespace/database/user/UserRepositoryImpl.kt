package com.hero.recipespace.database.user

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.data.user.local.UserLocalDataSource
import com.hero.recipespace.data.user.remote.UserRemoteDataSource
import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.mapper.toData
import com.hero.recipespace.domain.user.mapper.toEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import com.hero.recipespace.domain.user.request.LoginUserRequest
import com.hero.recipespace.domain.user.request.SignUpUserRequest
import com.hero.recipespace.domain.user.request.UpdateUserRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun login(request: LoginUserRequest): UserEntity {
        return userRemoteDataSource.login(request).toEntity()
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

    override suspend fun addUser(request: SignUpUserRequest) {
        val result = userRemoteDataSource.add(request)
        userLocalDataSource.add(result)
    }

    override suspend fun updateUser(
        userEntity: UserEntity
    ) {
        val result = userRemoteDataSource.update(userEntity.toData())
        userLocalDataSource.update(result)
    }

    // 레시피를 업로드하는 것과 유사하게 함수를 짜야할 수도 있어서 만들어놓음
    override suspend fun updateUser(request: UpdateUserRequest, onProgress: (Float) -> Unit) : UserEntity {
        val result = userRemoteDataSource.updateUser(request, onProgress)
        userLocalDataSource.update(result)

        return result.toEntity()
    }

    override suspend fun deleteUser(
        userEntity: UserEntity
    ) {
        val result = userRemoteDataSource.remove(userEntity.toData())
        userLocalDataSource.remove(result)
    }

    override suspend fun signOut() {
        return userRemoteDataSource.signOut()
    }

    private fun getEntities(data: List<UserData>): List<UserEntity> {
        val result = mutableListOf<UserEntity>()

        for (i in data.indices) {
            result.add(i, data[i].toEntity())
        }

        return result
    }
}