package com.hero.recipespace.ext

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

@BindingAdapter("timestamp")
fun TextView.setTimestamp(timestamp: Timestamp?) {
    timestamp ?: return

    val simpleDateFormat = SimpleDateFormat("mm:ss")
    text = simpleDateFormat.format(timestamp.toDate())
}