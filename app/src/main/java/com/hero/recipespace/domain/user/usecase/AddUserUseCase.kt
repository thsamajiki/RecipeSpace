package com.hero.recipespace.domain.user.usecase

import com.hero.recipespace.domain.user.repository.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun invoke(userName: String, email: String, pwd: String
    ) = userRepository.addUser(userName, email, pwd)
}