package com.hero.recipespace.data.rate.local

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.listener.OnCompleteListener

interface RateLocalDataSource {
    suspend fun getData(rateKey: String, onCompleteListener: OnCompleteListener<RateData>)

    fun getDataList(onCompleteListener: OnCompleteListener<List<RateData>>)

    fun clear()

    suspend fun add(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>)

    suspend fun update(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>)

    suspend fun remove(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>)
}