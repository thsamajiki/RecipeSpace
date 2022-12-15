package com.hero.recipespace.domain.user.usecase

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import com.hero.recipespace.domain.user.request.UpdateUserRequest
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userEntity: UserEntity) {
        userRepository.updateUser(userEntity)
    }

//    suspend operator fun invoke(newUserName: String = "", newProfileImageUrl: String = "") {
//        userRepository.updateUserInfo(newUserName, newProfileImageUrl)
//    }

    suspend operator fun invoke(
        request: UpdateUserRequest,
        onProgress: (Float) -> Unit)
            : Result<UserEntity> =
        kotlin.runCatching {
            userRepository.updateUser(request, onProgress)
        }
}