package com.hero.recipespace.ext

import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("imageURI")
fun AppCompatImageView.setImageUrl(uri: Uri?) {
    Glide.with(context)
        .load(uri)
        .into(this)
}

@BindingAdapter("imageURI")
fun ShapeableImageView.setImageUrl(uri: Uri?) {
    Glide.with(context)
        .load(uri)
        .into(this)
}