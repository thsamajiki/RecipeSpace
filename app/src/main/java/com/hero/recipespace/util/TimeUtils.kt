package com.hero.recipespace.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {
    // 시간 관련 함수들을 클래스로 모아둠

    fun convertTimeFormat(date: Date?, format: String?): String? {
        date ?: return null
        val currentDate = Date(System.currentTimeMillis()).time
        val diff = (currentDate - date.time) / 1000
        val dateFormat = SimpleDateFormat(format, Locale.getDefault()).format(date)

        return when (diff) {
            in 0 until 10 -> "지금 막"
            in 10 until 60 -> "${diff}초 전"
            in 60 until 60 * 60 -> "${diff / 60}분 전"
            in 60 * 60 until 60 * 60 * 24 -> "${diff / (60 * 60)}시간 전"
            in 60 * 60 * 24 until 60 * 60 * 48 -> "어제"
            in 60 * 60 * 48 until 60 * 60 * 72 -> "그저께"
            in 60 * 60 * 72 until 60 * 60 * 24 * 7 -> "${diff / (60 * 60 * 24)}일 전"
            else -> dateFormat
        }
    }
}