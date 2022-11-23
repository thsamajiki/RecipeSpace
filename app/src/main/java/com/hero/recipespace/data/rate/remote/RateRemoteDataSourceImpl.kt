package com.hero.recipespace.data.rate.remote

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.listener.OnCompleteListener

class RateRemoteDataSourceImpl : RateRemoteDataSource {
    override fun getData(rateKey: String, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        rateKey: String,
        onCompleteListener: OnCompleteListener<List<RateData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun update(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun remove(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }
}