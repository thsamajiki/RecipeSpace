package com.hero.recipespace.domain.user.mapper

import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.domain.user.entity.UserEntity

fun UserData.toEntity(): UserEntity {
    return UserEntity(
        key = key,
        name = name,
        email = email,
        profileImageUrl = profileImageUrl
    )
}

fun UserEntity.toData(): UserData {
    return UserData(
        key = key,
        name = name,
        email = email,
        profileImageUrl = profileImageUrl
    )
}