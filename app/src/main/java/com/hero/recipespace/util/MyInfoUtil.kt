package com.hero.recipespace.util

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.authentication.FirebaseAuthentication

class MyInfoUtil {   // 나의 정보에 관한 것을 단말기에 저장하거나 단말기로 불러오는 클래스
    // 나의 정보에 관한 것을 단말기에 저장하거나 단말기로 불러오는 클래스

    val EXTRA_EMAIL = "email"
    val EXTRA_PWD = "pwd"
    val EXTRA_NICKNAME = "nickname"
    val EXTRA_PROFILE_IMAGE_URL = "profileImageUrl"
    val EXTRA_RECIPE_IMAGE = "recipeImageUrl"
    val EXTRA_RECIPE_CONTENT = "recipeContent"

    companion object {
        private var instance: MyInfoUtil? = null

        fun getInstance(): MyInfoUtil {
            return instance ?: synchronized(this) {
                instance ?: MyInfoUtil().also {
                    instance = it
                }
            }
        }
    }

    fun getKey(): String? {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return null
        return firebaseUser.uid
    }

    fun getUserName(context: Context): String? {
        return SharedPreference.getInstance().getValue(context, EXTRA_NICKNAME, "")
    }

    fun getEmail(context: Context): String? {
        return SharedPreference.getInstance().getValue(context, EXTRA_EMAIL, "")
    }

    fun getPwd(context: Context): String? {
        return SharedPreference.getInstance().getValue(context, EXTRA_PWD, "")
    }

    fun getProfileImageUrl(context: Context): String? {
        return SharedPreference.getInstance().getValue(context, EXTRA_PROFILE_IMAGE_URL, "")
    }

    fun getRecipeImage(context: Context): String? {
        return SharedPreference.getInstance().getValue(context, EXTRA_RECIPE_IMAGE, "")
    }

    fun getRecipeContent(context: Context): String? {
        return SharedPreference.getInstance().getValue(context, EXTRA_RECIPE_CONTENT, "")
    }

    fun putUserName(context: Context, nickname: String) {
        SharedPreference.getInstance().put(context, EXTRA_NICKNAME, nickname)
    }

    fun putEmail(context: Context, email: String) {
        SharedPreference.getInstance().put(context, EXTRA_EMAIL, email)
    }

    fun putPwd(context: Context, pwd: String) {
        SharedPreference.getInstance().put(context, EXTRA_PWD, pwd)
    }

    fun putProfileImageUrl(context: Context, profileImageUrl: String) {
        SharedPreference.getInstance().put(context, EXTRA_PROFILE_IMAGE_URL, profileImageUrl)
    }

    fun putRecipeImage(context: Context, recipeImageUrl: String) {
        SharedPreference.getInstance().put(context, EXTRA_RECIPE_IMAGE, recipeImageUrl)
    }

    fun putRecipeContent(context: Context, recipeContent: String) {
        SharedPreference.getInstance().put(context, EXTRA_RECIPE_CONTENT, recipeContent)
    }

    fun signOut(context: Context) {
        SharedPreference.getInstance().remove(context)
        FirebaseAuthentication.getInstance().signOut()
    }
}