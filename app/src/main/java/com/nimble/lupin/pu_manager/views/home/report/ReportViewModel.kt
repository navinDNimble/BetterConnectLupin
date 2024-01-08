package com.nimble.lupin.pu_manager.views.home.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nimble.lupin.pu_manager.api.ApiService
import org.koin.java.KoinJavaComponent

class ReportViewModel : ViewModel() {
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)

}