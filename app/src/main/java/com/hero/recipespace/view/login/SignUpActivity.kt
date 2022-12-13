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
import com.google.firebase.auth.FirebaseUser
import com.hero.recipespace.MainActivity
import com.hero.recipespace.authentication.FirebaseAuthentication
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.databinding.ActivitySignUpBinding
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response
import com.hero.recipespace.view.login.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignUpBinding

    private val firebaseAuthentication: FirebaseAuthentication =
        FirebaseAuthentication.getInstance()
    private val firebaseData: FirebaseData = FirebaseData.getInstance()

    private val viewModel by viewModels<SignUpViewModel>()

    companion object {
        fun getIntent(context: Context) =
            Intent(context, SignUpActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        with(viewModel) {

        }
    }

    private fun setupListeners() {
        binding.btnSignUp.setOnClickListener {
            requestSignUp()
        }
    }

    private fun requestSignUp() {
        val email = binding.editEmail.text.toString()
        val pwd: String = binding.editPwd.text.toString()
        val userName: String = binding.editUsername.text.toString()
        if (!checkEmailValid(email)) {
            Toast.makeText(this, "이메일 양식을 확인해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.signUpUserAccount(userName, email, pwd)
    }

    private fun checkEmailValid(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false
        } else Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun signUp() {
        val userName: String = binding.editUsername.text.toString()
        val firebaseUser: FirebaseUser? = firebaseAuthentication.getCurrentUser()
        val userData = UserData(
            userKey = firebaseUser?.uid,
            userName = userName,
            email = firebaseUser?.email,
            profileImageUrl = firebaseUser?.photoUrl.toString()
        )

        firebaseData.signUp(this, userData, object : OnCompleteListener<Void> {
            override fun onComplete(isSuccess: Boolean, response: Response<Void>?) {
                if (isSuccess) {
                    intentMain()
                } else {
                    Toast.makeText(this@SignUpActivity, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        })
    }

    private fun intentMain() {
        val intent = MainActivity.getIntent(this)
        startActivity(intent)
        finishAffinity()
    }

    override fun onClick(view: View) {
    }
}