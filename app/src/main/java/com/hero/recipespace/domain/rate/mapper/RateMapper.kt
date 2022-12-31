package com.hero.recipespace.domain.rate.mapper

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.domain.rate.entity.RateEntity

fun RateData.toEntity(): RateEntity {
    return RateEntity(
        rateKey = rateKey,
        rate = rate,
        date = date
    )
}

fun RateEntity.toData(): RateData {
    return RateData(
        rateKey = rateKey,
        rate = rate,
        date = date
    )
}