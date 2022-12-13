package com.hero.recipespace.data.recipe.remote

import com.google.firebase.Timestamp
import com.hero.recipespace.data.recipe.RecipeData

interface RecipeRemoteDataSource {
    suspend fun getData(recipeKey: String) : RecipeData

    suspend fun getDataList() : List<RecipeData>

    suspend fun add(profileImageUrl : String,
                    userName: String,
                    userKey: String,
                    desc: String,
                    photoUrlList: List<String>,
                    postDate: Timestamp
    ) : RecipeData

    suspend fun update(recipeData: RecipeData) : RecipeData

    suspend fun remove(recipeData: RecipeData) : RecipeData
}