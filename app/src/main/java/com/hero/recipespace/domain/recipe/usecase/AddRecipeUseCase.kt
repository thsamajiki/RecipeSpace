package com.hero.recipespace.domain.recipe.usecase

import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import com.hero.recipespace.listener.OnCompleteListener
import javax.inject.Inject

class AddRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend fun invoke(recipeEntity: RecipeEntity, onCompleteListener: OnCompleteListener<RecipeEntity>) =
        recipeRepository.addRecipe(recipeEntity, onCompleteListener)
}