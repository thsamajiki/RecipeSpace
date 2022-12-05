package com.hero.recipespace.domain.user.usecase

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import com.hero.recipespace.listener.OnCompleteListener

class GetUserListUseCase(
    private val userRepository: UserRepository
) {
    fun invoke(onCompleteListener: OnCompleteListener<List<UserEntity>>) {
        userRepository.getUserList(onCompleteListener)
    }
}