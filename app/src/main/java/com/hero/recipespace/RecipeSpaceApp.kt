package com.hero.recipespace

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipeSpaceApp : Application() {

    companion object {
        lateinit var instance : RecipeSpaceApp

        fun getInstance() : Application {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}