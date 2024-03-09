package com.hero.recipespace.ext

import android.widget.RatingBar
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData

@BindingAdapter("rating")
fun RatingBar.setRatingScore(ratingLiveData: MutableLiveData<Float>) {
    this.rating = ratingLiveData.value ?: 0f

    setOnRatingBarChangeListener { _: RatingBar, rating, isUserNecessary ->
        if (isUserNecessary) {
            ratingLiveData.value = rating
        }
    }
}
