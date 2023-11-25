package com.nimble.lupin.admin.api

import com.nimble.lupin.admin.models.AdminProfileData
import com.nimble.lupin.admin.models.AdminTaskCountModel
import com.nimble.lupin.admin.models.TaskModel
import com.nimble.lupin.admin.models.TaskUpdatesModel
import com.nimble.lupin.admin.models.TaskUsersModel
import com.nimble.lupin.admin.models.UserModel
import com.nimble.lupin.admin.models.UserTasksListModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("check_mobile_number")
    fun checkMobileNumber(@Query("mobileNumber") mobile: String): Call<ResponseHandler<AdminProfileData>>
    @GET("admin_task_counts")
    fun getAdminTaskCount(): Call<ResponseHandler<AdminTaskCountModel>>
    @GET("get_all_task")
    fun getTask(@Query("page") page: Int): Call<ResponseHandler<List<TaskModel>>>

    @GET("get_task_users")
    fun getTaskUsers(@Query("taskId") taskId: Int, @Query("page") page: Int): Call<ResponseHandler<List<TaskUsersModel>>>

    @GET("get_all_user")
    fun getAllUserList(@Query("page") page: Int , @Query("searchKey") searchKey: String ): Call<ResponseHandler<List<UserModel>>>

    @GET("get_user_task_pending")
    fun getPendingUserTask(@Query("userId") userId: Int,@Query("page") page: Int): Call<ResponseHandler<List<UserTasksListModel>>>
    @GET("get_user_task_completed")
    fun getCompletedUserTask(@Query("userId") userId: Int,@Query("page") page: Int): Call<ResponseHandler<List<UserTasksListModel>>>

    @GET("get_update_task_details")
    fun getUserTaskDetails(@Query("userTaskId") userTaskId: Int): Call<ResponseHandler<List<TaskUpdatesModel>>>
    @POST("create_user")
    fun createUser(@Body userModel: UserModel): Call<ResponseHandler<UserModel>>
}