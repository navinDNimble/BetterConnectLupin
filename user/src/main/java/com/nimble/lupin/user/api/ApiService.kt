package com.nimble.lupin.user.api

import com.nimble.lupin.user.models.TaskModel
import com.nimble.lupin.user.models.UserProfileModel
import com.nimble.lupin.user.models.UserTaskCountModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
   @GET("check_mobile_number")
   fun checkMobileNumber(@Query("mobileNumber") mobile: String): Call<ResponseHandler<UserProfileModel>>
   @GET("user_task_counts")
   fun getUserTaskCount(@Query("userId") userId: Int): Call<ResponseHandler<UserTaskCountModel>>
   @GET("get_user_task_pending")
   fun getPendingUserTask(@Query("userId") userId: Int,@Query("page") page: Int): Call<ResponseHandler<List<TaskModel>>>
   @GET("get_user_task_completed")
   fun getCompletedUserTask(@Query("userId") userId: Int,@Query("page") page: Int): Call<ResponseHandler<List<TaskModel>>>
}