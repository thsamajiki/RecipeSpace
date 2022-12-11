package com.hero.recipespace.domain.user.usecase

import com.hero.recipespace.domain.user.entity.UserEntity
import com.hero.recipespace.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserListUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() : Flow<List<UserEntity>> =
        userRepository.observeUserList()
}