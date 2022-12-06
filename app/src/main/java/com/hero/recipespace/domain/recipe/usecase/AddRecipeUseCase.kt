package com.hero.recipespace.domain.recipe.usecase

import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import javax.inject.Inject

class AddRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipeEntity: RecipeEntity) =
        recipeRepository.addRecipe(recipeEntity)
}