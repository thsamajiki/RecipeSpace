package com.hero.recipespace.listener

interface OnCompleteListener<T> {
    fun onComplete(isSuccess: Boolean, response: Response<T>?)
}