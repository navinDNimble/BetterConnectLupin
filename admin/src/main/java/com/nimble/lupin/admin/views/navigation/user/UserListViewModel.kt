package com.nimble.lupin.admin.views.navigation.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nimble.lupin.admin.api.ApiService
import com.nimble.lupin.admin.api.ResponseHandler
import com.nimble.lupin.admin.models.TaskModel
import com.nimble.lupin.admin.models.UserModel
import com.nimble.lupin.admin.utils.Constants
import org.koin.java.KoinJavaComponent
import retrofit2.Call
import retrofit2.Response

class UserListViewModel : ViewModel(){
    var page =0
    var searchKey =""
    var isLastPage = false
    var responseError : MutableLiveData<String> = MutableLiveData()

    var loadingProgressBar : MutableLiveData<Boolean> = MutableLiveData()
    val taskListResponse = MutableLiveData<List<UserModel>>()
    private val apiService: ApiService by KoinJavaComponent.inject(ApiService::class.java)

    private var call: Call<ResponseHandler<List<UserModel>>>? = null
    fun getUsersList(){
        loadingProgressBar.postValue(true)
        call?.cancel()
        call = apiService.getAllUserList(Constants.AdminWorkStation_ID,page, searchKey)
        call?.enqueue(object : retrofit2.Callback<ResponseHandler<List<UserModel>>> {
            override fun onResponse(
                call: Call<ResponseHandler<List<UserModel>>>,
                response: Response<ResponseHandler<List<UserModel>>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("sachinAdminTASK", result.toString())
                    when (result?.code) {
                        200 -> {
                            isLastPage = result.isLastPage
                            taskListResponse.postValue(result.response!!)
                        }
                        404 -> {
                            Log.d("sachinAdminTASK", result.toString())
                            isLastPage = result.isLastPage
                            responseError.postValue(result.message)
                        }
                        409 -> {
                            Log.d("sachinAdminTASK", result.toString())
                            isLastPage = result.isLastPage
                        }
                        500 -> {
                            Log.d("sachinAdminTASK", result.toString())
                            responseError.postValue("Error in Loading Users" + result.message)
                        }
                    }
                }
                loadingProgressBar.postValue(false)
            }

            override fun onFailure(call: Call<ResponseHandler<List<UserModel>>>, t: Throwable) {
                Log.d("sachinscheduletask", t.message.toString())
                loadingProgressBar.postValue(false)
                responseError.postValue(t.message.toString())
            }
        })

    }
}