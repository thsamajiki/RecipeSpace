package com.hero.recipespace.domain.rate.request

data class AddRateRequest(
    val userKey: String,
    val rate: Float
)