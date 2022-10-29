package com.hero.recipespace

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat.postDelayed
import com.hero.recipespace.authentication.FirebaseAuthentication
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response
import com.hero.recipespace.view.login.LoginActivity

class SplashActivity : AppCompatActivity(), Runnable, OnCompleteListener<Void> {

    private val firebaseAuthentication: FirebaseAuthentication =
        FirebaseAuthentication.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        firebaseAuthentication.setOnCompleteListener(this)

        Handler(Looper.getMainLooper()).postDelayed(this, 1000)
    }

    override fun run() {
        firebaseAuthentication.autoLogin(this)
    }

    override fun onComplete(isSuccess: Boolean, response: Response<Void>?) {
        if (isSuccess) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}