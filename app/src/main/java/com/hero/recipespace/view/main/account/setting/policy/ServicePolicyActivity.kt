package com.hero.recipespace.view.main.account.setting.policy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hero.recipespace.databinding.ActivityServicePolicyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicePolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServicePolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityServicePolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun getIntent(context: Context) =
            Intent(context, ServicePolicyActivity::class.java)
    }
}