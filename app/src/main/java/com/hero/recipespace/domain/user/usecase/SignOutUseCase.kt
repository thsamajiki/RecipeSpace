package com.hero.recipespace.domain.user.usecase

import com.hero.recipespace.domain.user.repository.UserRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        kotlin.runCatching { userRepository.signOut() }
}