package com.hero.recipespace.database.recipe.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.recipe.dao.RecipeDao

@Database(entities = [RecipeData::class], version = 1)
abstract class AppRecipeDatabase : RoomDatabase() {
    abstract fun recipeDao() : RecipeDao

    companion object {
        @Volatile
        private var instance: AppRecipeDatabase? = null

        fun getInstance(context: Context): AppRecipeDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppRecipeDatabase::class.java, "recipe_db"
            ).build()
    }
}