package com.nimble.lupin.user.views.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.nimble.lupin.user.R
import com.nimble.lupin.user.utils.Constants
import com.nimble.lupin.user.views.home.MainActivity
import com.nimble.lupin.user.views.login.LoginActivity
import org.koin.java.KoinJavaComponent

class SplashActivity : AppCompatActivity() {
    private val sharedPref: SharedPreferences by KoinJavaComponent.inject(SharedPreferences::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            if (FirebaseAuth.getInstance().currentUser == null || sharedPref.getInt(
                    Constants.User_ID,
                    0
                ) == 0
            ) {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }else {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }

        }, 2000)


    }
}