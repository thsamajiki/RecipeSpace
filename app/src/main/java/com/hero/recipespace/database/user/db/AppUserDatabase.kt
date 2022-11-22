package com.hero.recipespace.database.user.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.user.dao.UserDao

@Database(entities = [UserData::class], version = 1)
abstract class AppUserDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao

    companion object {
        @Volatile
        private var instance: AppUserDatabase? = null

        fun getInstance(context: Context): AppUserDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppUserDatabase::class.java, "user_db"
            ).build()
    }
}