package com.hero.recipespace.domain.rate.request

data class UpdateRateRequest(
    val userKey: String,
    val rate: Float,
)
