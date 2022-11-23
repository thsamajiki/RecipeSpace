package com.hero.recipespace.database.recipe

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import com.hero.recipespace.listener.OnCompleteListener

class RecipeRepositoryImpl : RecipeRepository {
    override fun getRecipe(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>) {
        TODO("Not yet implemented")
    }

    override fun getRecipeList(onCompleteListener: OnCompleteListener<List<RecipeData>>) {
        TODO("Not yet implemented")
    }

    override fun addRecipe(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>) {
        TODO("Not yet implemented")
    }

    override fun modifyRecipe(
        recipeKey: String,
        onCompleteListener: OnCompleteListener<RecipeData>,
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteRecipe(
        recipeKey: String,
        onCompleteListener: OnCompleteListener<RecipeData>,
    ) {
        TODO("Not yet implemented")
    }
}