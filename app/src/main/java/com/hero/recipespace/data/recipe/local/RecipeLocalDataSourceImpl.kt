package com.hero.recipespace.data.recipe.local

import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.recipe.datastore.RecipeCacheStore
import com.hero.recipespace.database.recipe.datastore.RecipeLocalStore
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class RecipeLocalDataSourceImpl(
    private val recipeLocalStore: RecipeLocalStore,
    private val recipeCacheStore: RecipeCacheStore
) : RecipeLocalDataSource {

    override fun getData(recipeKey: String, onCompleteListener: OnCompleteListener<RecipeData>) {
        recipeCacheStore.getData(recipeKey, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    recipeLocalStore.getData(recipeKey, object : OnCompleteListener<RecipeData> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<RecipeData>?
                        ) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, null)
                            }
                        }
                    })
                }
            }
        })
    }

    override fun getDataList(onCompleteListener: OnCompleteListener<List<RecipeData>>
    ) {
        recipeCacheStore.getDataList(object : OnCompleteListener<List<RecipeData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>?) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    recipeLocalStore.getDataList(object : OnCompleteListener<List<RecipeData>> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<List<RecipeData>>?
                        ) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
                                onCompleteListener.onComplete(false, null)
                            }
                        }
                    })
                }
            }
        })
    }

    override fun clear() {
        recipeCacheStore.clear()
    }

    override fun add(recipeData: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        recipeLocalStore.add(recipeData, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                if (isSuccess) {
                    recipeCacheStore.add(recipeData, object : OnCompleteListener<RecipeData> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<RecipeData>?
                        ) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
//                                onCompleteListener.onComplete(true, response)
                            }
                        }
                    })
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override fun update(
        recipeData: RecipeData,
        onCompleteListener: OnCompleteListener<RecipeData>,
    ) {
        recipeLocalStore.update(recipeData, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                if (isSuccess) {
                    recipeCacheStore.update(recipeData, object : OnCompleteListener<RecipeData> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<RecipeData>?
                        ) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
//                                onCompleteListener.onComplete(true, response)
                            }
                        }
                    })
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }

    override fun remove(
        recipeData: RecipeData,
        onCompleteListener: OnCompleteListener<RecipeData>,
    ) {
        recipeLocalStore.remove(recipeData, object : OnCompleteListener<RecipeData> {
            override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                if (isSuccess) {
                    recipeCacheStore.remove(recipeData, object : OnCompleteListener<RecipeData> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<RecipeData>?
                        ) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, response)
                            } else {
//                                onCompleteListener.onComplete(true, response)
                            }
                        }
                    })
                } else {
                    onCompleteListener.onComplete(false, null)
                }
            }
        })
    }
}