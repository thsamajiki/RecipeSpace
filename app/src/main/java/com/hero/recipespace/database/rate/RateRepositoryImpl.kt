package com.hero.recipespace.database.rate

import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.rate.local.RateLocalDataSource
import com.hero.recipespace.data.rate.remote.RateRemoteDataSource
import com.hero.recipespace.domain.rate.repository.RateRepository
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class RateRepositoryImpl(
    private val rateLocalDataSource: RateLocalDataSource,
    private val rateRemoteDataSource: RateRemoteDataSource
) : RateRepository {

    override fun getRate(rateKey: String, onCompleteListener: OnCompleteListener<RateData>) {
        rateLocalDataSource.getData(rateKey, object : OnCompleteListener<RateData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    rateRemoteDataSource.getData(rateKey, object : OnCompleteListener<RateData> {
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

    override fun getRateList(onCompleteListener: OnCompleteListener<List<RateData>>) {
        rateLocalDataSource.getDataList(object : OnCompleteListener<List<RateData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<RateData>>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    rateRemoteDataSource.getDataList(object : OnCompleteListener<List<RateData>> {
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

    override fun addRate(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateRemoteDataSource.add(rateData, object : OnCompleteListener<RateData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                if (isSuccess) {
                    rateLocalDataSource.add(rateData, object : OnCompleteListener<RateData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, response)
                            }
                        }
                    })
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override fun modifyRate(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateRemoteDataSource.update(rateData, object : OnCompleteListener<RateData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                if (isSuccess) {
                    rateLocalDataSource.update(rateData, object : OnCompleteListener<RateData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, response)
                            }
                        }
                    })
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override fun deleteRate(rateData: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        rateRemoteDataSource.remove(rateData, object : OnCompleteListener<RateData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                if (isSuccess) {
                    rateLocalDataSource.remove(rateData, object : OnCompleteListener<RateData> {
                        override fun onComplete(isSuccess: Boolean, response: Response<RateData>?) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, response)
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