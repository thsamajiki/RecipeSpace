package com.hero.recipespace.database.user.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.user.UserData

@Dao
interface UserDao {
    @Query("SELECT * FROM user_db ORDER BY key ASC")
    fun getAllUsers() : LiveData<List<UserData>>

    @Query("SELECT * FROM user_db WHERE key = :userKey limit 1")
    suspend fun getUserFromKey(key: String?): UserData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userData: UserData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userDataList: List<UserData>)

    @Update
    fun updateUser(userData: UserData)

    @Delete
    fun deleteUser(userData: UserData)
}