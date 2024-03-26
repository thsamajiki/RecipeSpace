package com.hero.recipespace.view.login

import androidx.annotation.StringRes
import com.hero.recipespace.R

enum class InvalidSignUpInfoType(
    @StringRes private val message: Int,
) {
    EMPTY_EMAIL(R.string.empty_email),
    EMPTY_PWD(R.string.empty_password),
    EMPTY_USER_NAME(R.string.empty_user_name),
    INVALID_EMAIL_FORM(R.string.invalid_email_form),
    INVALID_PWD_LENGTH(R.string.invalid_password_length),
    INCORRECT_PWD(R.string.incorrect_password),
    DUPLICATED_ACCOUNT(R.string.duplicated_account),
    FAILED_SIGN_UP(R.string.failed_sign_up)
}
