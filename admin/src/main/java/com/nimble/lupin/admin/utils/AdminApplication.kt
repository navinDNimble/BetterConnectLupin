package com.nimble.lupin.admin.utils

import android.app.AlertDialog
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.nimble.lupin.admin.api.ApiService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level
import org.koin.dsl.module
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class AdminApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@AdminApplication)
            modules(listOf(apiModule))
        }


    }
    private val apiModule = module {
        fun provideUseApi(): ApiService {

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService::class.java)
        }
        fun provideSharedPref() : SharedPreferences {
            return getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)
        }
        single { provideUseApi() }
        single { provideSharedPref() }
    }

}
