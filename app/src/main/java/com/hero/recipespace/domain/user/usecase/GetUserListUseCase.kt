package com.hero.recipespace.domain.user.usecase

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import com.hero.recipespace.listener.OnCompleteListener
import javax.inject.Inject

class GetUserListUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun invoke(onCompleteListener: OnCompleteListener<List<UserEntity>>) =
        userRepository.observeUserList(onCompleteListener)
}