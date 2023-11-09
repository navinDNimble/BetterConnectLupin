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
import com.nimble.lupin.user.R
import com.nimble.lupin.user.utils.Constants
import com.nimble.lupin.user.views.home.MainActivity
import com.nimble.lupin.user.views.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setSplashScreenTheme(R.style.Base_Theme_BetterConnect)
            splashScreen.setOnExitAnimationListener {
               changeActivity()
            }
        }else{
            setContentView(R.layout.activity_splash)
            Handler(Looper.getMainLooper()).postDelayed({
                changeActivity()
            }, 3000)
        }

    }
    private fun changeActivity(){
        val intent  :Intent
        val sharedPref = getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)
        intent = if ((sharedPref.getInt(Constants.User_ID,0) == 0).not()){
            Intent(this, MainActivity::class.java)


        }else{
            Intent(this, LoginActivity::class.java)

        }
        startActivity(intent)
        finish()
//        with(sharedPref.edit()) {
//            putString(key, value)
//            apply()
//        }

    }
}