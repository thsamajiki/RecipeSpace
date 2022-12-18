package com.hero.recipespace.domain.user.request

data class SignUpUserRequest(
    val email: String,
    val name: String,
    val pwd: String
)
