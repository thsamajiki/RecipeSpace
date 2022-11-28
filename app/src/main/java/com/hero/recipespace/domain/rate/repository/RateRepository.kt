package com.hero.recipespace.domain.rate.repository

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.listener.OnCompleteListener

interface RateRepository {
    suspend fun getRate(rateKey: String, onCompleteListener: OnCompleteListener<RateData>)

    fun getRateList(onCompleteListener: OnCompleteListener<List<RateData>>)

    suspend fun addRate(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>)

    suspend fun modifyRate(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>)

    suspend fun deleteRate(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>)
}