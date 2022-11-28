package com.hero.recipespace.database.chat.datastore

import android.content.Context
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.CacheStore
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class ChatCacheStore: CacheStore<ChatData>() {

    companion object {
        private lateinit var instance : ChatCacheStore

        fun getInstance(context: Context) : ChatCacheStore {
            return instance ?: synchronized(this) {
                instance ?: ChatCacheStore().also {
                    instance = it
                }
            }
        }
    }

    override suspend fun getData(vararg params: Any, onCompleteListener: OnCompleteListener<ChatData>) {
        val chatKey: String = params[0].toString()

        for (chatData in object : OnCompleteListener<List<RecipeData>> {
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
        }) {
            if (chatData.key == chatKey) {
                onCompleteListener.onComplete(true, chatData)
                return
            }
        }
    }

    override fun getDataList(vararg params: Any, onCompleteListener: OnCompleteListener<List<ChatData>>) {
        if (getDataList().isEmpty()) {
            onCompleteListener.onComplete(true, null)
        } else {
            onCompleteListener.onComplete(false, getDataList())
        }
    }

    override fun add(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
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

    override fun update(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
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

    override fun remove(data: ChatData, onCompleteListener: OnCompleteListener<ChatData>) {
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