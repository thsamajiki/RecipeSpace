package com.hero.recipespace.data.rate.local

import androidx.lifecycle.LiveData
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.database.rate.datastore.RateCacheStore
import com.hero.recipespace.database.rate.datastore.RateLocalStore
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class RateLocalDataSourceImpl(
    private val rateLocalStore: RateLocalStore,
    private val rateCacheStore: RateCacheStore
) : RateLocalDataSource {

    override suspend fun getData(rateKey: String, onCompleteListener: OnCompleteListener<RateData>): LiveData<RateData> {
        rateCacheStore.getData(rateKey, object : OnCompleteListener<RateData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    rateLocalStore.getData(rateKey, object : OnCompleteListener<RateData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, null)
                            }
                        }
                    })
                }
            }
        })
    }

    override fun getDataList(
        onCompleteListener: OnCompleteListener<List<RateData>>
    ): LiveData<List<RateData>> {
        rateCacheStore.getDataList(object : OnCompleteListener<List<RateData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<RateData>>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    rateLocalStore.getDataList(object : OnCompleteListener<List<RateData>> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<List<RateData>>?
                        ) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, null)
                            }
                        }
                    })
                }
            }
        })
    }

    override fun clear() {
        rateCacheStore.clear()
    }

    override suspend fun add(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateLocalStore.add(rateData, object : OnCompleteListener<RateData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                if (isSuccess) {
                    rateCacheStore.add(rateData, object : OnCompleteListener<RateData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, null)
                            }
                        }
                    })
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override suspend fun update(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateLocalStore.update(rateData, object : OnCompleteListener<RateData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                if (isSuccess) {
                    rateCacheStore.update(rateData, object : OnCompleteListener<RateData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, null)
                            }
                        }
                    })
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override suspend fun remove(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateLocalStore.remove(rateData, object : OnCompleteListener<RateData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                if (isSuccess) {
                    rateCacheStore.remove(rateData, object : OnCompleteListener<RateData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, null)
                            }
                        }
                    })
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }
}