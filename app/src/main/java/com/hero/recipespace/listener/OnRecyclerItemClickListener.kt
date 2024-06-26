package com.hero.recipespace.listener

import android.view.View

interface OnRecyclerItemClickListener<T> {
    fun onItemClick(
        position: Int,
        view: View,
        data: T,
    )
}
