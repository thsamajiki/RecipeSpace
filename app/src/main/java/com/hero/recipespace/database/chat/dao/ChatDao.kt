package com.hero.recipespace.database.chat.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hero.recipespace.data.chat.ChatData

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_db ORDER BY key ASC")
    fun getAllChats() : LiveData<List<ChatData>>

    @Query("SELECT * FROM chat_db WHERE id = :key limit 1")
    suspend fun getMusicFileFromKey(key: String?): ChatData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusic(chatData: ChatData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chatDataList: List<ChatData>)

    @Delete
    fun deleteMusic(chatData: ChatData)
}