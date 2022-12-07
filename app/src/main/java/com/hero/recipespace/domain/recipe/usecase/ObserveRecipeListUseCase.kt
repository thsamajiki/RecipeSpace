package com.hero.recipespace.domain.recipe.usecase

import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRecipeListUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    operator fun invoke() : Flow<List<RecipeEntity>> =
        recipeRepository.observeRecipeList()
}