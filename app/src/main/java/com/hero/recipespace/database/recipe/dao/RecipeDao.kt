package com.hero.recipespace.database.recipe.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hero.recipespace.data.recipe.RecipeData

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe_db ORDER BY postDate DESC")
    fun observeAllRecipes(): LiveData<List<RecipeData>>

    @Query("SELECT * FROM recipe_db WHERE `key` = :key limit 1")
    suspend fun getRecipeFromKey(key: String?): RecipeData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeData: RecipeData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipeDataList: List<RecipeData>)

    @Update
    suspend fun updateRecipe(recipeData: RecipeData)

    @Delete
    suspend fun deleteRecipe(recipeData: RecipeData)
}
