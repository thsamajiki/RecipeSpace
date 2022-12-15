package com.hero.recipespace.domain.recipe.usecase

import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import com.hero.recipespace.domain.recipe.request.UpdateRecipeRequest
import javax.inject.Inject

class UpdateRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipeEntity: RecipeEntity) =
        recipeRepository.modifyRecipe(recipeEntity)

    // 레시피를 업로드하는 것과 유사하게 함수를 짜야 할수도 있어서 만들어놓음
    suspend operator fun invoke(
        request: UpdateRecipeRequest,
        onProgress: (Float) -> Unit
    ) : Result<RecipeEntity> =
        kotlin.runCatching {
            recipeRepository.modifyRecipe(request, onProgress)
        }
}