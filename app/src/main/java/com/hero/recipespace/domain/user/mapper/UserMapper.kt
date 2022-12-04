package com.hero.recipespace.domain.user.mapper

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.domain.user.entity.UserEntity

fun UserData.toEntity(): UserEntity {
    return UserEntity(
        userKey = userKey,
        userName = userName,
        profileImageUrl = profileImageUrl
    )
}