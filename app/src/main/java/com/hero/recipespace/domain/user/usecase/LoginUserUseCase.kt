package com.hero.recipespace.domain.user.usecase

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import com.hero.recipespace.domain.user.request.LoginUserRequest
import javax.inject.Inject

class LoginUserUseCase
@Inject
constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(request: LoginUserRequest): Result<UserEntity> =
        kotlin.runCatching {
            userRepository.login(request)
        }
}
