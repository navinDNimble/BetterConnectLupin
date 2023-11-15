package com.nimble.lupin.user.views.home.task

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimble.lupin.user.api.ApiService
import com.nimble.lupin.user.models.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class TaskViewModel : ViewModel()  {

    val progressRecyclerVisibility = ObservableField(false)
    val completedRecyclerVisibility = ObservableField(false)
    val taskList = MutableLiveData<List<TaskModel>>()

    private val apiService: ApiService by inject(ApiService::class.java)

     fun getUserTask(userId: Int ){

             viewModelScope.launch(Dispatchers.IO) {

//                 //val response = apiService.getUserAllTask(userId).execute()
//
//                 if (response.isSuccessful) {
//                     val result = response.body()
//                     if (result != null ) {
//                        // taskList.postValue()
//                     }
//                 }
//                 else {
//
//                     val errorBody = response.errorBody()
//
//                 }
             }
    }

}