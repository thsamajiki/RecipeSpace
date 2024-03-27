package com.hero.recipespace.view.login

import androidx.annotation.StringRes
import com.hero.recipespace.R

enum class InvalidLoginInfoType(
    @StringRes val message: Int,
) {
    INVALID_EMAIL_FORM(R.string.invalid_email_form),
    EMPTY_EMAIL(R.string.empty_email),
    EMPTY_PWD(R.string.empty_password),
    FAILED_LOGIN(R.string.failed_login)
}
