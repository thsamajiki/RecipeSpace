package com.hero.recipespace.domain.user.request

data class LoginUserRequest(
    val email: String,
    val pwd: String
)
