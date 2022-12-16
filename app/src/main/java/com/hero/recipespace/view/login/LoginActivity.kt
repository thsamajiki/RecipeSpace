package com.hero.recipespace.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hero.recipespace.MainActivity
import com.hero.recipespace.databinding.ActivityLoginBinding
import com.hero.recipespace.listener.Response
import com.hero.recipespace.view.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

//    private val firebaseAuthentication: FirebaseAuthentication = FirebaseAuthentication.getInstance()

    companion object {
        fun getIntent(context: Context) =
            Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupViewModel()
        setupListeners()
//        firebaseAuthentication.setOnCompleteListener(this)
    }

    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {

            }
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            requestLogin()
        }

        binding.btnSignUp.setOnClickListener {
            val intent = SignUpActivity.getIntent(this)
            startActivity(intent)
        }
    }

    private fun requestLogin() {
        val email: String = binding.editEmail.text.toString().trim()
        val pwd: String = binding.editPwd.text.toString().trim()

        if (!checkEmailValid(email)) {
            Toast.makeText(this, "이메일 양식을 확인해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
//        firebaseAuthentication.login(this, email, pwd)
    }

    private fun checkEmailValid(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false
        } else Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun onComplete(isSuccess: Boolean, response: Response<Void>?) {
        if (isSuccess) {
            val intent = MainActivity.getIntent(this)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "로그인에 실패했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(view: View) {
    }
}