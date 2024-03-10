package com.hero.recipespace.domain.user.usecase

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import javax.inject.Inject

class RemoveUserUseCase
@Inject
constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(userEntity: UserEntity): Result<Unit> =
        kotlin.runCatching { userRepository.deleteUser(userEntity) }
}
