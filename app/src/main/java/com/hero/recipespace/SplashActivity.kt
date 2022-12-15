package com.hero.recipespace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.listener.Response
import com.hero.recipespace.view.login.LoginActivity
import java.util.*

class SplashActivity : AppCompatActivity(), Runnable {

//    private val firebaseAuthentication: FirebaseAuthentication =
//        FirebaseAuthentication.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        Handler(Looper.getMainLooper()).postDelayed(this, 1000)
        checkAutoLogin()
    }

    override fun run() {

    }

    private fun checkAutoLogin() {
        if (currentUser == null) {
            Timer().schedule(object : TimerTask() {
                override fun run() {

                    val intent = LoginActivity.getIntent(applicationContext)
                    startActivity(intent)
                }
            }, 1000)
        } else {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    val intent = MainActivity.getIntent(applicationContext)
                    startActivity(intent)
                }
            }, 1000)
        }
        finish()
    }

    fun onComplete(isSuccess: Boolean, response: Response<Void>?) {
        if (isSuccess) {
            val intent = MainActivity.getIntent(this)
            startActivity(intent)
        } else {
            val intent = LoginActivity.getIntent(this)
            startActivity(intent)
        }
        finish()
    }
}