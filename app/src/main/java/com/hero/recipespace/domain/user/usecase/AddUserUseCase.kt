package com.hero.recipespace.domain.user.usecase

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.OnFailedListener
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun invoke(userName: String,
                       email: String,
                       pwd: String,
                       onCompleteListener: OnCompleteListener<UserEntity>,
                       onFailedListener: OnFailedListener
    ) = userRepository.addUser(userName, email, pwd, onCompleteListener, onFailedListener)
}