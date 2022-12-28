package com.hero.recipespace.domain.rate.mapper

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.domain.rate.entity.RateEntity

fun RateData.toEntity(): RateEntity {
    return RateEntity(
        key = key,
        userKey = userKey,
        rate = rate,
        date = date
    )
}

fun RateEntity.toData(): RateData {
    return RateData(
        key = key,
        userKey = userKey,
        rate = rate ?: 0f,
        date = date
    )
}