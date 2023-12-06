package com.nimble.lupin.user.api

import com.nimble.lupin.user.models.TaskModel
import com.nimble.lupin.user.models.TaskUpdatesModel
import com.nimble.lupin.user.models.UserProfileModel
import com.nimble.lupin.user.models.UserTaskCountModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
   @GET("get_update_task_details")
   fun getUserTaskDetails(@Query("userTaskId") userTaskId: Int): Call<ResponseHandler<List<TaskUpdatesModel>>>
   @POST("update_task_details")
   fun updateUserTaskDetails(@Body updatesModel: TaskUpdatesModel): Call<ResponseHandler<TaskUpdatesModel>>

   @GET("get_photos_url")
   fun getPhotosUrl(@Query("taskUpdateId") taskUpdateId: Int): Call<ResponseHandler<List<String>>>
}