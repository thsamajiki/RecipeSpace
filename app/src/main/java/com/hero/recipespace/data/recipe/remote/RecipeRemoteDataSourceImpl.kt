package com.hero.recipespace.data.recipe.remote

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.recipe.service.RecipeService
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.domain.recipe.request.UpdateRecipeRequest
import com.hero.recipespace.domain.recipe.request.UploadRecipeRequest
import javax.inject.Inject

class RecipeRemoteDataSourceImpl @Inject constructor(
    private val recipeService: RecipeService
) : RecipeRemoteDataSource {
    override suspend fun getData(recipeKey: String) : RecipeData {
        return recipeService.getRecipe(recipeKey)
    }

    override suspend fun getDataList() : List<RecipeData> {
        return recipeService.getRecipeList()
    }

    override suspend fun add(
        request: UploadRecipeRequest,
        onProgress: (Float) -> Unit
        ) : RecipeData {

        val downloadUrls = recipeService.uploadImages(
            request.recipePhotoPathList,
            progress = onProgress
        )

        val userName: String = FirebaseAuth.getInstance().currentUser?.displayName.orEmpty()
        val userKey: String = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        val profileImageUrl: String = FirebaseAuth.getInstance().currentUser?.photoUrl?.toString().orEmpty()

        Log.d("zxcv", "add: userName : $userName")
        Log.d("zxcv", "add: userKey : $userKey")
        Log.d("zxcv", "add: profileImageUrl : $profileImageUrl")

        return recipeService.add(
            profileImageUrl,
            userName,
            userKey,
            request.content,
            downloadUrls,
            Timestamp.now()
        )
    }

    private fun getFirebaseAuthProfile(): UserData {
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        val profileImageUrl: String = firebaseUser?.photoUrl.toString()

        return UserData(
            firebaseUser?.uid.orEmpty(),
            firebaseUser?.displayName,
            firebaseUser?.email,
            profileImageUrl
        )
    }

    // 레시피를 업로드하는 것과 유사하게 함수를 짜야 할수도 있어서 만들어놓음
    override suspend fun update(
        request: UpdateRecipeRequest,
        onProgress: (Float) -> Unit
    ): RecipeData {

        return recipeService.update(request, onProgress)
    }

    override suspend fun remove(
        recipeData: RecipeData
    ) : RecipeData {
        return recipeService.remove(recipeData)
    }
}