package com.hero.recipespace.data.rate.local

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.listener.OnCompleteListener

interface RateLocalDataSource {
    fun getData(rateKey: String, onCompleteListener: OnCompleteListener<RateData>)

    fun getDataList(onCompleteListener: OnCompleteListener<List<RateData>>)

    fun clear()

    fun add(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>)

    fun update(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>)

    fun remove(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>)
}