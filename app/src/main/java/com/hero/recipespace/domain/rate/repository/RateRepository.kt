package com.hero.recipespace.domain.rate.repository

import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.listener.OnCompleteListener

interface RateRepository {
    suspend fun getRate(rateKey: String, onCompleteListener: OnCompleteListener<RateEntity>)

    fun getRateList(onCompleteListener: OnCompleteListener<List<RateEntity>>)

    suspend fun addRate(rateEntity: RateEntity, onCompleteListener: OnCompleteListener<RateEntity>)

    suspend fun modifyRate(rateEntity: RateEntity, onCompleteListener: OnCompleteListener<RateEntity>)

    suspend fun deleteRate(rateEntity: RateEntity, onCompleteListener: OnCompleteListener<RateEntity>)
}