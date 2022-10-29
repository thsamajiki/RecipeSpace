package com.hero.recipespace.view.photoview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityPhotoBinding

class PhotoActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPhotoBinding
    val EXTRA_PHOTO_URL = "photoUrl"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        loadImage()
    }

    private fun getPhotoUrl(): String? {
        return intent.getStringExtra(EXTRA_PHOTO_URL)
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun loadImage() {
        Glide.with(this).load(getPhotoUrl()).into(binding.photoView)
    }

    override fun onClick(v: View?) {
    }
}