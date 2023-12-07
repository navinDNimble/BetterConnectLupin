package com.nimble.lupin.admin.api

import com.nimble.lupin.admin.models.AdminProfileData
import com.nimble.lupin.admin.models.AdminTaskCountModel
import com.nimble.lupin.admin.models.AssignTaskBody
import com.nimble.lupin.admin.models.AssignTaskModel
import com.nimble.lupin.admin.models.TaskCreateResponseModel
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
    @GET("check_manager_mobile_number")
    fun checkMobileNumber(@Query("mobileNumber") mobile: String): Call<ResponseHandler<AdminProfileData>>
    @GET("admin_task_counts")
    fun getAdminTaskCount(): Call<ResponseHandler<AdminTaskCountModel>>
    @GET("get_all_task")
    fun getAllTask(@Query("page") page: Int ,@Query("searchKey") searchKey: String ): Call<ResponseHandler<List<TaskModel>>>

    @GET("get_task_to_assign")
    fun getTaskToAssign(@Query("page") page: Int ,@Query("searchKey") searchKey: String ): Call<ResponseHandler<List<TaskModel>>>

    @GET("get_admin_schedule_task")
    fun getScheduleTask(@Query("page") page: Int ,@Query("searchKey") searchKey: String ): Call<ResponseHandler<List<TaskModel>>>

    @GET("get_task_users")
    fun getTaskUsers(@Query("taskId") taskId: Int, @Query("page") page: Int): Call<ResponseHandler<List<TaskUsersModel>>>

    @GET("get_all_user")
    fun getAllUserList(@Query("page") page: Int , @Query("searchKey") searchKey: String ): Call<ResponseHandler<List<UserModel>>>

    @GET("get_all_authority")
    fun getAllAuthority(): Call<ResponseHandler<List<UserModel>>>
    @GET("get_user_task_pending")
    fun getPendingUserTask(@Query("userId") userId: Int,@Query("page") page: Int): Call<ResponseHandler<List<UserTasksListModel>>>
    @GET("get_user_task_completed")
    fun getCompletedUserTask(@Query("userId") userId: Int,@Query("page") page: Int): Call<ResponseHandler<List<UserTasksListModel>>>

    @GET("get_update_task_details")
    fun getUserTaskDetails(@Query("userTaskId") userTaskId: Int): Call<ResponseHandler<List<TaskUpdatesModel>>>
    @POST("create_user")
    fun createUser(@Body userModel: UserModel): Call<ResponseHandler<UserModel>>
    @GET("activities")
    fun getActivitySubActivityTaskMode(): Call<ResponseHandler<TaskCreateResponseModel>>
    @POST("create_task")
    fun createTask(@Body taskModel: TaskModel): Call<ResponseHandler<TaskModel>>

    @GET("get_all_user")
    fun getAllUserForSelectionList(@Query("page") page: Int , @Query("searchKey") searchKey: String ): Call<ResponseHandler<List<AssignTaskModel>>>

    @POST("/assign_users_to_task")
    fun allotTaskToUser(@Body assignTaskModelList : AssignTaskBody): Call<ResponseHandler<AssignTaskBody>>

    @GET("get_photos_url")
    fun getPhotosUrl(@Query("taskUpdateId") taskUpdateId: Int): Call<ResponseHandler<List<String>>>
}