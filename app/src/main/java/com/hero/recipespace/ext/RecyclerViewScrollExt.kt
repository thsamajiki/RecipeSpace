package com.hero.recipespace.ext

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 세로 스크롤 가능 여부 확인
 * */
fun RecyclerView.isScrollable(): Boolean {
    return canScrollVertically(1) || canScrollVertically(-1)
}

/**
 * StackFromEnd 설정
 * */
fun RecyclerView.setStackFromEnd() {
    (layoutManager as? LinearLayoutManager)?.stackFromEnd = true
}
