package com.nimble.lupin.admin.views.navigation.createtask

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nimble.lupin.admin.api.ApiService
import com.nimble.lupin.admin.api.ResponseHandler
import com.nimble.lupin.admin.models.ActivityModel
import com.nimble.lupin.admin.models.SubActivityModel
import com.nimble.lupin.admin.models.TaskCreateResponseModel
import com.nimble.lupin.admin.models.TaskModeModel
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateTaskViewModel : ViewModel() {


    var activityResponse = MutableLiveData<List<ActivityModel>>()
    var subActivityResponse = MutableLiveData<List<SubActivityModel>>()
    var taskModeResponse = MutableLiveData<List<TaskModeModel>>()
    var responseError = MutableLiveData<String>()

    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    fun getActivitySubActivityTaskMode() {

        apiService.getActivitySubActivityTaskMode()
            .enqueue(object : Callback<ResponseHandler<TaskCreateResponseModel>> {
                override fun onResponse(
                    call: Call<ResponseHandler<TaskCreateResponseModel>>,
                    response: Response<ResponseHandler<TaskCreateResponseModel>>
                ) {
                    val result = response.body()
                    if (result?.code == 200) {
                        Log.d("sachinCreateTask", result.response.toString())
                        activityResponse.postValue(result.response.activities)
                        subActivityResponse.postValue(result.response.subActivities)
                        taskModeResponse.postValue(result.response.taskModes)


                    } else {
                        Log.d("sachinCreateTask", result?.response.toString())
                        responseError.postValue(result?.message.toString())

                    }

                }

                override fun onFailure(
                    call: Call<ResponseHandler<TaskCreateResponseModel>>,
                    t: Throwable
                ) {
                    responseError.postValue(t.message.toString())
                }

            })

    }
}