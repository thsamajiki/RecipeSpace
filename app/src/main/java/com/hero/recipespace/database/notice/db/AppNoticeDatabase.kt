package com.hero.recipespace.database.notice.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.database.notice.dao.NoticeDao
import com.hero.recipespace.ext.TypeConverterExt

@Database(entities = [NoticeData::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverterExt::class)
abstract class AppNoticeDatabase : RoomDatabase() {
    abstract fun noticeDao(): NoticeDao

    companion object {
        @Volatile
        private var instance: AppNoticeDatabase? = null

        fun getInstance(context: Context): AppNoticeDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppNoticeDatabase::class.java,
                "notice_db",
            ).build()
    }
}
