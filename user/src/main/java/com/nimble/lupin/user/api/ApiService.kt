package com.nimble.lupin.user.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/api/v1/user/tasks/{id}")
     fun getUserAllTask(@Path("id") id: Int) :Call<ResponseBody>
}