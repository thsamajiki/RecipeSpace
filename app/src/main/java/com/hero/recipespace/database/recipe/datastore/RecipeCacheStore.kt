package com.hero.recipespace.database.recipe.datastore

import android.content.Context
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.CacheStore
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class RecipeCacheStore : CacheStore<RecipeData>() {

    companion object {
        private lateinit var instance : RecipeCacheStore

        fun getInstance(context: Context) : RecipeCacheStore {
            return instance ?: synchronized(this) {
                instance ?: RecipeCacheStore().also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<RecipeData>) {
        val recipeKey: String = params[0].toString()

        for (recipeData in getDataList(object :
            OnCompleteListener<List<RecipeData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    recipeLocalStore.getDataList(object :
                        OnCompleteListener<List<RecipeData>> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<List<RecipeData>>
                        ) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
        })) {
            if (recipeData.key.equals(recipeKey)) {
                onCompleteListener.onComplete(true, recipeData)
                return
            }
        }
    }

    override fun getDataList(
        vararg params: Any,
        onCompleteListener: OnCompleteListener<List<RecipeData>>,
    ) {
        if (getDataList(object :
            OnCompleteListener<List<RecipeData>> {
                override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>) {
                    if (isSuccess) {
                        onCompleteListener.onComplete(true, response)
                    } else {
                        recipeLocalStore.getDataList(object :
                            OnCompleteListener<List<RecipeData>> {
                            override fun onComplete(
                                isSuccess: Boolean,
                                response: Response<List<RecipeData>>
                            ) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }).isEmpty()) {
            onCompleteListener.onComplete(true, null)
        } else {
            onCompleteListener.onComplete(false, getDataList(object :
                OnCompleteListener<List<RecipeData>> {
                override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>) {
                    if (isSuccess) {
                        onCompleteListener.onComplete(true, response)
                    } else {
                        recipeLocalStore.getDataList(object :
                            OnCompleteListener<List<RecipeData>> {
                            override fun onComplete(
                                isSuccess: Boolean,
                                response: Response<List<RecipeData>>
                            ) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }))
        }
    }

    override fun add(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        getDataList(object :
            OnCompleteListener<List<RecipeData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    recipeLocalStore.getDataList(object :
                        OnCompleteListener<List<RecipeData>> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<List<RecipeData>>
                        ) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
        }).add(data)

        if (onCompleteListener != null) {
            onCompleteListener.onComplete(true, data)
        }
    }

    override fun update(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        val originIndex = getDataList(object :
            OnCompleteListener<List<RecipeData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    recipeLocalStore.getDataList(object :
                        OnCompleteListener<List<RecipeData>> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<List<RecipeData>>
                        ) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
        }).indexOf(data)
        if (originIndex == -1) {
            throw IndexOutOfBoundsException("기존 데이터가 없습니다.")
        } else {
            getDataList(object :
                OnCompleteListener<List<RecipeData>> {
                override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>) {
                    if (isSuccess) {
                        onCompleteListener.onComplete(true, response)
                    } else {
                        recipeLocalStore.getDataList(object :
                            OnCompleteListener<List<RecipeData>> {
                            override fun onComplete(
                                isSuccess: Boolean,
                                response: Response<List<RecipeData>>
                            ) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }).set(originIndex, data)
        }

        if (onCompleteListener != null) {
            onCompleteListener.onComplete(true, data)
        }
    }

    override fun remove(data: RecipeData, onCompleteListener: OnCompleteListener<RecipeData>) {
        val originIndex = getDataList(object :
            OnCompleteListener<List<RecipeData>> {
            override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, response)
                } else {
                    recipeLocalStore.getDataList(object :
                        OnCompleteListener<List<RecipeData>> {
                        override fun onComplete(
                            isSuccess: Boolean,
                            response: Response<List<RecipeData>>
                        ) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
        }).indexOf(data)
        if (originIndex == -1) {
            throw IndexOutOfBoundsException("기존 데이터가 없습니다.")
        } else {
            getDataList(object :
                OnCompleteListener<List<RecipeData>> {
                override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>) {
                    if (isSuccess) {
                        onCompleteListener.onComplete(true, response)
                    } else {
                        recipeLocalStore.getDataList(object :
                            OnCompleteListener<List<RecipeData>> {
                            override fun onComplete(
                                isSuccess: Boolean,
                                response: Response<List<RecipeData>>
                            ) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }).remove(originIndex)
        }
    }
}