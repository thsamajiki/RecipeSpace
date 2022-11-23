package com.hero.recipespace.domain.rate.repository

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.listener.OnCompleteListener

interface RateRepository {
    fun getRate(rateKey: String, onCompleteListener: OnCompleteListener<RateData>)

    fun getRateList(onCompleteListener: OnCompleteListener<List<RateData>>)

    fun addRate(rateKey: String, onCompleteListener: OnCompleteListener<RateData>)

    fun modifyRate(rateKey: String, onCompleteListener: OnCompleteListener<RateData>)

    fun deleteRate(rateKey: String, onCompleteListener: OnCompleteListener<RateData>)
}