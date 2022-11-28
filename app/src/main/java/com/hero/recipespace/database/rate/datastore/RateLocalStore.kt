package com.hero.recipespace.database.rate.datastore

import android.content.Context
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.database.LocalStore
import com.hero.recipespace.listener.OnCompleteListener

class RateLocalStore(
    private val context: Context
) : LocalStore<RateData>(context) {

    companion object {
        private lateinit var instance : RateLocalStore

        fun getInstance(context: Context) : RateLocalStore {
            return instance ?: synchronized(this) {
                instance ?: RateLocalStore(context).also {
                    instance = it
                }
            }
        }
    }

    override suspend fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<RateData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(data: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun update(data: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }

    override fun remove(data: RateData, onCompleteListener: OnCompleteListener<RateData>) {
        TODO("Not yet implemented")
    }
}