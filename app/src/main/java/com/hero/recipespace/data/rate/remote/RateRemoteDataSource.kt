package com.hero.recipespace.data.rate.remote

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.listener.OnCompleteListener

interface RateRemoteDataSource {
    fun getData(rateKey: String, onCompleteListener: OnCompleteListener<RateData>)

    fun getDataList(onCompleteListener: OnCompleteListener<List<RateData>>)

    fun add(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>)

    fun update(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>)

    fun remove(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>)
}