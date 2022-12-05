package com.hero.recipespace.domain.recipe.usecase

import androidx.lifecycle.LiveData
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import com.hero.recipespace.listener.OnCompleteListener

class GetRecipeUseCase(
    private val recipeRepository: RecipeRepository
) {
    suspend fun invoke(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeEntity>): LiveData<RecipeEntity> {
        return recipeRepository.getRecipe(recipeKey, onCompleteListener)
    }
}