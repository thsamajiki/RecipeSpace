package com.hero.recipespace.database.chat.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hero.recipespace.data.chat.ChatData

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_db ORDER BY `key` ASC")
    fun getAllChats() : LiveData<List<ChatData>>

    @Query("SELECT * FROM chat_db WHERE `key` = :key limit 1")
    fun getChatFromKey(key: String?): ChatData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chatData: ChatData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chatDataList: List<ChatData>)

    @Update
    fun updateChat(chatData: ChatData)

    @Delete
    fun deleteChat(chatData: ChatData)
}