package com.hero.recipespace.ext

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@BindingAdapter("timestamp")
fun TextView.setTimestamp(timestamp: Timestamp?) {
    timestamp ?: return

    val calendar = Calendar.getInstance()

    val currentDate = calendar.time.time
    val diff = (currentDate - timestamp.toDate().time) / 1000
    val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(timestamp.toDate())

    text = when (diff) {
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

@BindingAdapter("messageTimestamp")
fun TextView.setMessageTimestamp(timestamp: Timestamp?) {
    timestamp ?: return

    val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault()).format(timestamp.toDate())
    val netDate = Date(milliseconds)
    val date = timeFormat.format(netDate)
    val hour = date.split(":")[0]
    val minute = date.split(":")[1]


    text = when(hour.toInt()) {
        in 0 until 12 -> "오전 ${hour}:${minute}"
        12 -> "오후 ${hour}:${minute}"
        else -> "오후 ${hour.toInt() - 12}:${minute}"
    }
}

@BindingAdapter("messageDatestamp")
fun TextView.setMessageDatestamp(timestamp: Timestamp?) {
    timestamp ?: return

    val calendar = Calendar.getInstance()

    val currentDate = calendar.time.time
    val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(timestamp.toDate())

    text = dateFormat
}