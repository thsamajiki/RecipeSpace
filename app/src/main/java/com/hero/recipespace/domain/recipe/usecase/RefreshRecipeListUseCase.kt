package com.hero.recipespace.domain.recipe.usecase

import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import javax.inject.Inject

class RefreshRecipeListUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return kotlin.runCatching { recipeRepository.refresh() }
    }
}