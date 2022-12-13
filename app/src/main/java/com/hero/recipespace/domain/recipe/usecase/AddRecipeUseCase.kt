package com.hero.recipespace.domain.recipe.usecase

import com.google.firebase.Timestamp
import com.hero.recipespace.domain.recipe.repository.RecipeRepository
import javax.inject.Inject

class AddRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(profileImageUrl : String,
                                userName: String,
                                userKey: String,
                                desc: String,
                                photoUrlList: List<String>,
                                postDate: Timestamp) =
        recipeRepository.addRecipe(profileImageUrl, userName, userKey, desc, photoUrlList, postDate)
}