package com.nimble.lupin.user.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.nimble.lupin.user.api.ApiService
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.core.scope.get
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UserApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@UserApplication)
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
        fun provideSharedPref() : SharedPreferences{
            return getSharedPreferences(Constants.SHARED_PREF_KEY,Context.MODE_PRIVATE)
        }
        single { provideUseApi() }
        single { provideSharedPref() }
    }

}