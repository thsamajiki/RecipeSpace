package com.hero.recipespace.view.login

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.hero.recipespace.MainActivity
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityLoginBinding
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.view.LoadingState
import com.hero.recipespace.view.login.viewmodel.LoginUiState
import com.hero.recipespace.view.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    companion object {
        fun getIntent(context: Context) =
            Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupView()
        setupViewModel()
        setupListeners()
    }

    private fun setupView() {
        binding.tvSignUp.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {
                loadingState.collect { state ->
                    when (state) {
                        LoadingState.Hidden -> hideLoading()
                        LoadingState.Idle -> {}
                        LoadingState.Loading -> showLoading()
                        is LoadingState.Progress -> setProgressPercent(state.value)
                    }
                }
            }

            lifecycleScope.launch {
                loginUiState.collect { state ->
                    when (state) {
                        is LoginUiState.Success -> {
                            val intent = MainActivity.getIntent(this@LoginActivity)
                            startActivity(intent)
                            finish()
                        }

                        is LoginUiState.Failed -> {
                            Toast.makeText(this@LoginActivity,
                                resources.getString(state.message),
                                Toast.LENGTH_SHORT,
                                ).show()
                        }

                        LoginUiState.Idle -> {}
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            requestLogin()
        }

        binding.tvSignUp.setOnClickListener {
            val intent = SignUpActivity.getIntent(this)
            startActivity(intent)
        }
    }

    private fun requestLogin() {
        val email = viewModel.email.value?.trim()
        val pwd = viewModel.pwd.value?.trim()

        if (email != null && pwd != null) {
            viewModel.requestLogin(email, pwd)
        }
    }

    override fun onClick(view: View) {
    }
}
