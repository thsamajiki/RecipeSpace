package com.hero.recipespace.domain.user.request

import com.hero.recipespace.domain.user.entity.Email
import com.hero.recipespace.domain.user.entity.Password

data class SignUpUserRequest(
    val email: Email,
    val name: String,
    val pwd: Password,
)
// java clean code
// 타입 분리도 하고 메모리 이슈도 해결함
