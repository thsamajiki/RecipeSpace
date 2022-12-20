package com.hero.recipespace.ext

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

//@BindingAdapter("imageUrl")
//fun ImageView.setImageUrl(uri: Uri?) {
//    Glide.with(context)
//        .load(uri)
//        .into(this)
//    setImageURI(uri)
//}

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}

//@BindingAdapter("imageURI")
//fun ShapeableImageView.setImageUrl(uri: Uri?) {
//    Glide.with(context)
//        .load(uri)
//        .into(this)
//}