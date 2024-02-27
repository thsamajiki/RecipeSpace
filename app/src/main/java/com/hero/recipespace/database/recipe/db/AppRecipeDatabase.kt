package com.hero.recipespace.database.recipe.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.recipe.dao.RecipeDao
import com.hero.recipespace.ext.TypeConverterExt

@Database(entities = [RecipeData::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverterExt::class)
abstract class AppRecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var instance: AppRecipeDatabase? = null

        fun getInstance(context: Context): AppRecipeDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppRecipeDatabase::class.java,
                "recipe_db",
            ).build()
    }
}
