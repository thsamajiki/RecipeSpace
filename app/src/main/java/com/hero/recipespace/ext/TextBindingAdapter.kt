package com.hero.recipespace.ext

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

@BindingAdapter("timestamp")
fun TextView.setTimestamp(timestamp: Timestamp?) {
    timestamp ?: return

    val simpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
//    val simpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분")
    text = simpleDateFormat.format(timestamp.toDate())
}