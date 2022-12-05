package com.hero.recipespace.domain.recipe.usecase

import androidx.lifecycle.LiveData
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import com.hero.recipespace.listener.OnCompleteListener

class GetRecipeListUseCase(
    private val recipeRepository: RecipeRepository
) {
    suspend fun invoke(onCompleteListener: OnCompleteListener<List<RecipeEntity>>) : LiveData<List<RecipeEntity>> {
        return recipeRepository.getRecipeList(onCompleteListener)
    }
}