package com.hero.recipespace.domain.rate.mapper

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.domain.rate.entity.RateEntity

fun RateData.toEntity(): RateEntity {
    return RateEntity(
        key = key,
        userKey = userKey,
        userName = userName,
        profileImageUrl = profileImageUrl,
        rate = rate,
        date = date
    )
}

fun RateData.toData(rateEntity: RateEntity): RateData {
    return RateData(
        key = rateEntity.key,
        userKey = rateEntity.userKey,
        userName = rateEntity.userName,
        profileImageUrl = rateEntity.profileImageUrl,
        rate = rateEntity.rate,
        date = rateEntity.date
    )
}