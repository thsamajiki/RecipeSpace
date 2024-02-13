package com.hero.recipespace.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.hero.recipespace.MainActivity
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivitySignUpBinding
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.view.LoadingState
import com.hero.recipespace.view.login.viewmodel.SignUpUiState
import com.hero.recipespace.view.login.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel by viewModels<SignUpViewModel>()

    companion object {
        fun getIntent(context: Context) =
            Intent(context, SignUpActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {
                signUpUiState.collect { state ->
                    when(state) {
                        is SignUpUiState.Failed -> Toast.makeText(this@SignUpActivity, state.message, Toast.LENGTH_SHORT).show()
                        is SignUpUiState.Success -> goToMainPage()
                        SignUpUiState.Idle -> {}
                    }
                }
            }

            lifecycleScope.launch {
                loadingState.collect { state ->
                    when(state) {
                        is LoadingState.Hidden -> hideLoading()
                        is LoadingState.Loading -> showLoading()
                        is LoadingState.Progress -> setProgressPercent(state.value)
                        LoadingState.Idle -> {}
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnSignUp.setOnClickListener {
            requestSignUp()
        }
    }

    private fun requestSignUp() {
        val email = binding.editEmail.text.toString().trim()
        val pwd: String = binding.editPwd.text.toString().trim()
        val userName: String = binding.editUserName.text.toString().trim()


        viewModel.signUpUserAccount(userName, email, pwd)
    }

    private fun goToMainPage() {
        val intent = MainActivity.getIntent(this@SignUpActivity)
        startActivity(intent)
        finishAffinity()
    }

    override fun onClick(view: View) {
    }
}