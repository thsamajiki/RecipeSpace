package com.hero.recipespace.view.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hero.recipespace.MainActivity
import com.hero.recipespace.authentication.FirebaseAuthentication
import com.hero.recipespace.databinding.ActivityLoginBinding
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener, OnCompleteListener<Void> {

    private lateinit var binding: ActivityLoginBinding

    private val firebaseAuthentication: FirebaseAuthentication = FirebaseAuthentication.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        firebaseAuthentication.setOnCompleteListener(this)
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(view: View) {
    }

    private fun login() {
        val email: String = binding.editEmail.text.toString()
        val pwd: String = binding.editPwd.text.toString()
        if (!checkEmailValid(email)) {
            Toast.makeText(this, "이메일 양식을 확인해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        firebaseAuthentication.login(this, email, pwd)
    }

    private fun checkEmailValid(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false
        } else Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onComplete(isSuccess: Boolean, response: Response<Void>?) {
        if (isSuccess) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "로그인에 실패했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
    }
}