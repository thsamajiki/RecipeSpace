package com.hero.recipespace.ext

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("imageURI")
fun ImageView.setImageUrl(uri: Uri?) {
    Glide.with(context)
        .load(uri)
        .into(this)
}

@BindingAdapter("imageURI")
fun ImageView.setImageUrl(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}

@BindingAdapter("imageURI")
fun ShapeableImageView.setImageUrl(uri: Uri?) {
    Glide.with(context)
        .load(uri)
        .into(this)
}