package com.nimble.lupin.admin.views.home.home

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nimble.lupin.admin.api.ApiService
import com.nimble.lupin.admin.api.ResponseHandler
import com.nimble.lupin.admin.models.AdminTaskCountModel
import com.nimble.lupin.admin.utils.Constants
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    var userName  = ObservableField("")
    var totalTask  = ObservableField("0")
    var pendingTask  = ObservableField("0")
    var completedTask  = ObservableField("0")
    var responseError : MutableLiveData<String> = MutableLiveData()

    fun getTasksStatus (){
        apiService.getAdminTaskCount(Constants.AdminWorkStation_ID).enqueue(object :
            Callback<ResponseHandler<AdminTaskCountModel>> {
            override fun onResponse(
                call: Call<ResponseHandler<AdminTaskCountModel>>,
                response: Response<ResponseHandler<AdminTaskCountModel>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result?.code == 200) {
                        Log.d("sachinHome",result.toString())
                        totalTask.set(result.response.total_task.toString())
                        completedTask.set(result.response.completed_task.toString())
                        pendingTask.set(result.response.pending_task.toString())
                    } else {
                        responseError.postValue(result?.message)

                    }
                    Log.d("sachinHome",result.toString())
                }
            }

            override fun onFailure(call: Call<ResponseHandler<AdminTaskCountModel>>, t: Throwable) {
                Log.d("sachinHome",t.message.toString())
                responseError.postValue(t.message.toString())
            }

        })
    }
}