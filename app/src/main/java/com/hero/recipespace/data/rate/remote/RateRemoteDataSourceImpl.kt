package com.hero.recipespace.data.rate.remote

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.database.rate.datastore.RateCloudStore
import com.hero.recipespace.listener.OnCompleteListener

class RateRemoteDataSourceImpl(
    private val rateCloudStore: RateCloudStore
) : RateRemoteDataSource {
    override suspend fun getData(rateKey: String, onCompleteListener: OnCompleteListener<RateData>) {
        rateCloudStore.getData(rateKey, onCompleteListener)
    }

    override fun getDataList(onCompleteListener: OnCompleteListener<List<RateData>>) {
        rateCloudStore.getDataList(onCompleteListener)
    }

    override suspend fun add(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateCloudStore.add(rateData, onCompleteListener)
    }

    override suspend fun update(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateCloudStore.update(rateData, onCompleteListener)
    }

    override suspend fun remove(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateCloudStore.remove(rateData, onCompleteListener)
    }
}