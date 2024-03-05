package com.hero.recipespace.view.login

import com.hero.recipespace.R

enum class InvalidLoginInfoType(
    private val message: Int,
) {
    INVALID_EMAIL_FORM(R.string.invalid_email_form),
    INCORRECT_PWD(R.string.incorrect_password),
    EMPTY_PWD(R.string.empty_password),
}
