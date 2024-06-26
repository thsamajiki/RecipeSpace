package com.hero.recipespace.domain.recipe.usecase

import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import javax.inject.Inject

class SearchRecipeUseCase
@Inject
constructor(
    private val recipeRepository: RecipeRepository,
) {
    suspend operator fun invoke(query: String): Result<List<RecipeEntity>> =
        kotlin.runCatching { recipeRepository.searchRecipe(query) }
}
