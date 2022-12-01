package com.hero.recipespace.domain.user.usecase

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import com.hero.recipespace.listener.OnCompleteListener

class GetUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun invoke(userKey: String, onCompleteListener: OnCompleteListener<UserEntity>) {
        userRepository.getUser(userKey, onCompleteListener)
    }
}