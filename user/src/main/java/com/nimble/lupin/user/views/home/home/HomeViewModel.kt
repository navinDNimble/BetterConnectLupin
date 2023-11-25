package com.nimble.lupin.user.views.home.home


import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.nimble.lupin.user.api.ApiService
import com.nimble.lupin.user.api.ResponseHandler
import com.nimble.lupin.user.models.UserTaskCountModel
import com.nimble.lupin.user.utils.Constants
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class HomeViewModel() : ViewModel() {
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)
    var userName  = ObservableField("")
    var totalTask  = ObservableField("0")
    var pendingTask  = ObservableField("0")
    var completedTask  = ObservableField("0")

    var responseError : MutableLiveData<String> = MutableLiveData()

    fun getTasksStatus (){
       apiService.getUserTaskCount(Constants.userId).enqueue(object :
           Callback<ResponseHandler<UserTaskCountModel>> {
           override fun onResponse(
               call: Call<ResponseHandler<UserTaskCountModel>>,
               response: Response<ResponseHandler<UserTaskCountModel>>
           ) {
               if (response.isSuccessful) {
                   val result = response.body()
                   if (result?.code == 200) {
                       totalTask.set(result.response.total_task.toString())
                       completedTask.set(result.response.completed_task.toString())
                       pendingTask.set(result.response.pending_task.toString())
                   } else {
                        responseError.postValue(result?.message)

                   }
                   Log.d("sachinHome",result.toString())
               }
           }

           override fun onFailure(call: Call<ResponseHandler<UserTaskCountModel>>, t: Throwable) {
               Log.d("sachinHome",t.message.toString())
               responseError.postValue(t.message.toString())
           }

       })
   }
}