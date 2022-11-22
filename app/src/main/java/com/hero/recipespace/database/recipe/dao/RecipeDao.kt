package com.hero.recipespace.database.recipe.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hero.recipespace.data.notice.NoticeData
import com.hero.recipespace.data.recipe.RecipeData

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe_db ORDER BY key ASC")
    fun getAllRecipes() : LiveData<List<RecipeData>> //

    @Query("SELECT * FROM recipe_db WHERE key = :key limit 1")
    suspend fun getRecipeFromKey(key: String?): RecipeData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipeData: RecipeData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipeDataList: List<RecipeData>)

    @Update
    fun updateRecipe(recipeData: RecipeData)

    @Delete
    fun deleteRecipe(recipeData: RecipeData)
}