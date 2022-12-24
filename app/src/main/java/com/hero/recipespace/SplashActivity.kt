package com.hero.recipespace

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.view.login.LoginActivity
import com.hero.recipespace.view.viewmodel.SplashUIState
import com.hero.recipespace.view.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity(), Runnable {

    val currentUser = FirebaseAuth.getInstance().currentUser

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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

    override fun run() {

    }
}