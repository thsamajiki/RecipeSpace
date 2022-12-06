package com.hero.recipespace.data.rate.remote

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.database.rate.datastore.RateCloudStore
import kotlinx.coroutines.flow.Flow

class RateRemoteDataSourceImpl(
    private val rateCloudStore: RateCloudStore
) : RateRemoteDataSource {
    override fun getData(rateKey: String) : Flow<RateData> {
        return rateCloudStore.getData(rateKey)
    }

    override fun getDataList() : Flow<List<RateData>> {
        return rateCloudStore.getDataList()
    }

    override suspend fun add(rateData: RateData) {
        rateCloudStore.add(rateData)
    }

    override suspend fun update(rateData: RateData) {
        rateCloudStore.update(rateData)
    }

    override suspend fun remove(rateData: RateData) {
        rateCloudStore.remove(rateData)
    }
}