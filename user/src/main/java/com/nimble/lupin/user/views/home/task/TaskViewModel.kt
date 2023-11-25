package com.nimble.lupin.user.views.home.task

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nimble.lupin.user.api.ApiService
import com.nimble.lupin.user.api.ResponseHandler
import com.nimble.lupin.user.models.TaskModel
import com.nimble.lupin.user.utils.Constants
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskViewModel : ViewModel()  {


    val pendingRecyclerViewVisibility = ObservableField(false)
    val completedRecyclerViewVisibility = ObservableField(false)
    val pendingTaskResultTextView = ObservableField("")
    val completedTaskResultTextView = ObservableField("")
    val LoadingPendingTask = ObservableField(false)
    val isLoadingCompletedTask = ObservableField(false)
    val pendingTaskTextVisibility  = ObservableField(false)
    val completedTaskTextVisibility = ObservableField(false)
    var isLastPageOfCompleted = false
    var isLastPageOfPending = false
    val completedTaskListResponse = MutableLiveData<List<TaskModel>>()
    val pendingTaskListResponse = MutableLiveData<List<TaskModel>>()
    val responseError = MutableLiveData<String>()



    //Frgament component

     var pendingPage = 0
     var completedPage = 0
    private val apiService: ApiService by inject(ApiService::class.java)

    fun getPendingUserTask(){
        Log.d("sachinTaskCompletedPage", pendingPage.toString())

        LoadingPendingTask.set(true)

        apiService.getPendingUserTask(Constants.userId,pendingPage).enqueue(object :Callback<ResponseHandler<List<TaskModel>>>{
            override fun onResponse(
                call: Call<ResponseHandler<List<TaskModel>>>,
                response: Response<ResponseHandler<List<TaskModel>>>
            ) {
                if (response.isSuccessful){
                    val result = response.body()
                    Log.d("sachinTaskPending", result.toString())
                    if (result?.code == 200){
                        pendingTaskListResponse.postValue(result.response!!)
                        pendingTaskTextVisibility.set(false)
                        isLastPageOfPending = result.isLastPage
                    }else if(result?.code == 404){
                         // No user Task Available
                        pendingTaskResultTextView.set(result.message)
                        pendingTaskTextVisibility.set(true)
                        isLastPageOfPending = result.isLastPage
                    }else if(result?.code == 409){

                        //No More Task To Load
                        isLastPageOfPending = result.isLastPage

                    }else if(result?.code == 500){
                           responseError.postValue("Error in Loading Pending Task"+result.message)
                        pendingTaskResultTextView.set("Error in Loading Pending Task"+result.message)
                        pendingTaskTextVisibility.set(true)
                    }
                    LoadingPendingTask.set(false)
                }

            }

            override fun onFailure(call: Call<ResponseHandler<List<TaskModel>>>, t: Throwable) {
                Log.d("sachinTaskPending", t.message.toString())
                LoadingPendingTask.set(false)
                responseError.postValue("Error in Loading Pending Task")
            }

        })
    }
     fun getCompletedUserTask(){
         Log.d("sachinTaskCompletedPage", completedPage.toString())
         isLoadingCompletedTask.set(true)
         apiService.getCompletedUserTask(Constants.userId,completedPage).enqueue(object :Callback<ResponseHandler<List<TaskModel>>>{
             override fun onResponse(
                 call: Call<ResponseHandler<List<TaskModel>>>,
                 response: Response<ResponseHandler<List<TaskModel>>>
             ) {
                 if (response.isSuccessful){
                     val result = response.body()
                     Log.d("sachinTaskCompleted", result.toString())
                     if (result?.code == 200){
                         completedTaskListResponse.postValue(result.response!!)
                         completedTaskTextVisibility.set(false)
                         isLastPageOfCompleted = result.isLastPage
                     }else if(result?.code == 404){
                         // NO user Task Available\
                         completedTaskResultTextView.set(result.message)
                         completedTaskTextVisibility.set(true)
                         isLastPageOfCompleted = result.isLastPage
                     }else if(result?.code == 409){
                           //No More Task To Load
                         isLastPageOfCompleted = result.isLastPage
                     }else if(result?.code == 404){

                     }else if(result?.code == 500){
                         responseError.postValue("Error in Loading Completed Task "+result.message)
                         completedTaskResultTextView.set("Error in Loading Completed Task"+result.message)
                         pendingTaskTextVisibility.set(true)
                     }
                 }
                 isLoadingCompletedTask.set(false)
             }

             override fun onFailure(call: Call<ResponseHandler<List<TaskModel>>>, t: Throwable) {
                 isLoadingCompletedTask.set(false)
                 responseError.postValue("Error in Loading Pending Task")
                 Log.d("sachinTaskCompleted", t.message.toString())
             }


         })
    }


}