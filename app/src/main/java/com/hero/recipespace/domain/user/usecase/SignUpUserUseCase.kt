package com.hero.recipespace.domain.user.usecase

import com.hero.recipespace.domain.user.repository.UserRepository
import com.hero.recipespace.domain.user.request.SignUpUserRequest
import javax.inject.Inject

class SignUpUserUseCase
@Inject
constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(request: SignUpUserRequest): Result<Unit> =
        kotlin.runCatching { userRepository.signUpUser(request) }
}
