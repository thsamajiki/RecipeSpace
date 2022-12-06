package com.hero.recipespace.ext

import android.widget.RatingBar
import androidx.databinding.BindingAdapter

@BindingAdapter("rating")
fun RatingBar.setRatingScore(rating: Float) {
    this.rating = rating
}