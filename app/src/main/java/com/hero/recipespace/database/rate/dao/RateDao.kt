package com.hero.recipespace.database.rate.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hero.recipespace.data.rate.RateData

@Dao
interface RateDao {
    @Query("SELECT * FROM rate_db ORDER BY `rateKey` ASC")
    fun getAllRates(): LiveData<List<RateData>>

    @Query("SELECT * FROM rate_db WHERE `rateKey` = :key limit 1")
    suspend fun getRateFromKey(key: String?): RateData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rateData: RateData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rateDataList: List<RateData>)

    @Update
    suspend fun updateRate(rateData: RateData)

    @Delete
    suspend fun deleteRate(rateData: RateData)
}
