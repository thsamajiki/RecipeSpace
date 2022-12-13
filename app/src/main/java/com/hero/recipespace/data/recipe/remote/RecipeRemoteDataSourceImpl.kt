package com.hero.recipespace.data.recipe.remote

import com.google.firebase.Timestamp
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.recipe.service.RecipeService
import javax.inject.Inject

class RecipeRemoteDataSourceImpl @Inject constructor(
    private val recipeService: RecipeService
) : RecipeRemoteDataSource {
    override suspend fun getData(recipeKey: String) : RecipeData {
        return recipeService.getRecipe(recipeKey)
    }

    override suspend fun getDataList() : List<RecipeData> {
        return recipeService.getRecipeList()
    }

    override suspend fun add(profileImageUrl : String,
                             userName: String,
                             userKey: String,
                             desc: String,
                             photoUrlList: List<String>,
                             postDate: Timestamp
    ) : RecipeData {
        return recipeService.add(profileImageUrl, userName, userKey, desc, photoUrlList, postDate)
    }

    override suspend fun update(
        recipeData: RecipeData
    ) : RecipeData {
        return recipeService.update(recipeData)
    }

    override suspend fun remove(
        recipeData: RecipeData
    ) : RecipeData {
        return recipeService.remove(recipeData)
    }
}