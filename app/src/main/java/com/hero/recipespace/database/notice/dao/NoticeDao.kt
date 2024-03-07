package com.hero.recipespace.database.notice.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hero.recipespace.data.notice.NoticeData

@Dao
interface NoticeDao {
    @Query("SELECT * FROM notice_db ORDER BY `key` ASC")
    fun getAllNotices(): LiveData<List<NoticeData>>

    @Query("SELECT * FROM notice_db WHERE `key` = :key limit 1")
    suspend fun getNoticeFromKey(key: String?): NoticeData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotice(noticeData: NoticeData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(noticeDataList: List<NoticeData>)

    @Update
    fun updateNotice(noticeData: NoticeData)

    @Delete
    fun deleteNotice(noticeData: NoticeData)
}
