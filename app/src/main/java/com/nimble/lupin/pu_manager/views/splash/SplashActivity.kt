package com.nimble.lupin.pu_manager.views.splash

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.nimble.lupin.pu_manager.R
import com.nimble.lupin.pu_manager.utils.Constants
import com.nimble.lupin.pu_manager.views.home.MainActivity
import com.nimble.lupin.pu_manager.views.login.LoginActivity
import org.koin.java.KoinJavaComponent

class SplashActivity : AppCompatActivity() {
    private val sharedPref: SharedPreferences by KoinJavaComponent.inject(SharedPreferences::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            if (FirebaseAuth.getInstance().currentUser == null || sharedPref.getInt(
                    Constants.Admin_ID_Key,
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