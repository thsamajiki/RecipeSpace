package com.hero.recipespace.view.login

import com.hero.recipespace.R

enum class InvalidUserInfoType(
    private val message: Int,
) {
    EMPTY_EMAIL(R.string.empty_email),
    EMPTY_PWD(R.string.empty_password),
    EMPTY_NAME(R.string.empty_user_name),
    INVALID_EMAIL_FORM(R.string.invalid_email_form),
    INVALID_PWD_LENGTH(R.string.invalid_password_length),
    INCORRECT_PASSWORD(R.string.incorrect_password),
}
