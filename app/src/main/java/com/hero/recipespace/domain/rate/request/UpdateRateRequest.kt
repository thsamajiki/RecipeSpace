package com.hero.recipespace.domain.rate.request

data class UpdateRateRequest(
    val key: String,
    val userKey: String,
    val rate: Float
)