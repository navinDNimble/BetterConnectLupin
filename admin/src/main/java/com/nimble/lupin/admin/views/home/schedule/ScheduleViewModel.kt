package com.nimble.lupin.admin.views.home.schedule

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nimble.lupin.admin.api.ApiService
import com.nimble.lupin.admin.api.ResponseHandler
import com.nimble.lupin.admin.models.TaskModel
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Response

class ScheduleViewModel : ViewModel() {
    var page =0
    var isLastPage = false
    var responseError : MutableLiveData<String> = MutableLiveData()
    var loadingProgressBar : MutableLiveData<Boolean> = MutableLiveData()
    val taskListResponse = MutableLiveData<List<TaskModel>>()
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)

    fun getTaskList(){
        loadingProgressBar.postValue(true)
        apiService.getTask(page , "").enqueue(object : retrofit2.Callback<ResponseHandler<List<TaskModel>>>{
            override fun onResponse(
                call: Call<ResponseHandler<List<TaskModel>>>,
                response: Response<ResponseHandler<List<TaskModel>>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("sachinAdminTASK",result.toString())
                    if (result?.code == 200) {
                        isLastPage = result.isLastPage
                       taskListResponse.postValue(result.response!!)

                    } else if(result?.code == 404){

                        isLastPage = result.isLastPage
                        responseError.postValue("No Tasks Available"+result.message)

                    }else if(result?.code == 409){
                        isLastPage = result.isLastPage
                    }else if(result?.code == 500){
                        responseError.postValue("Error in Loading  Task"+result.message)
                    }

                }
                loadingProgressBar.postValue(false)
            }

            override fun onFailure(call: Call<ResponseHandler<List<TaskModel>>>, t: Throwable) {
                Log.d("sachinscheduletask",t.message.toString())
                loadingProgressBar.postValue(false)
                responseError.postValue(t.message.toString())
            }

        })

    }
}