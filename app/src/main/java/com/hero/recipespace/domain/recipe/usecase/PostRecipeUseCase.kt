package com.hero.recipespace.domain.recipe.usecase

import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import com.hero.recipespace.domain.recipe.request.UploadRecipeRequest
import javax.inject.Inject

class PostRecipeUseCase
@Inject
constructor(
    private val recipeRepository: RecipeRepository,
) {
    suspend operator fun invoke(
        request: UploadRecipeRequest,
        onProgress: (Float) -> Unit,
    ): Result<RecipeEntity> =
        kotlin.runCatching {
            recipeRepository.addRecipe(request, onProgress)
        }
}
