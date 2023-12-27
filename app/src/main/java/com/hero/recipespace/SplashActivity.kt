package com.hero.recipespace

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hero.recipespace.databinding.ActivitySplashBinding
import com.hero.recipespace.view.login.LoginActivity
import com.hero.recipespace.view.viewmodel.SplashUIState
import com.hero.recipespace.view.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        viewModel.splashUiState.observe(this) { state ->
            when (state) {
                is SplashUIState.Success -> {
                    val intent = MainActivity.getIntent(this)
                    startActivity(intent)
                    finish()
                }

                is SplashUIState.Failed -> {
                    val intent = LoginActivity.getIntent(this)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setupView() {
        startAnimationWithShow(this, binding.ivCookingPan, R.anim.show01)
        startAnimationWithShow(this, binding.ivBalloons, R.anim.show02)
        startAnimationWithShow(this, binding.ivBottomCookLogo, R.anim.show03)
    }

    private fun startAnimationWithShow(context: Context, view: View, id: Int) {
        view.visibility = View.VISIBLE  // 애니메이션 전에 뷰를 보이게 한다
        view.startAnimation(AnimationUtils.loadAnimation(context, id)) // 애니메이션 설정 & 시작
    }
}