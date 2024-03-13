package com.hero.recipespace.domain.user.request

data class UpdateUserRequest(
    val newUserName: String,
    val newProfileImageUrl: String,
)
