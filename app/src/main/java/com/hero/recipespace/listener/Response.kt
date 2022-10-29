package com.hero.recipespace.listener

class Response<T> {
    private var type: Type? = null
    private var data: T? = null

    fun getType(): Type? {
        return type
    }

    fun setType(type: Type?) {
        this.type = type
    }

    fun getData(): T? {
        return data
    }

    fun setData(data: T) {
        this.data = data
    }

    fun isEmpty(): Boolean {
        return data == null
    }

    fun isNotEmpty(): Boolean {
        return data != null
    }
}