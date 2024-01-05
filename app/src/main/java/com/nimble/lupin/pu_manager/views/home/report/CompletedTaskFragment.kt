package com.nimble.lupin.pu_manager.views.home.report

import android.os.Bundle
import android.util.Log
import android.view.View
import com.nimble.lupin.pu_manager.api.ResponseHandler
import com.nimble.lupin.pu_manager.models.TaskModel
import com.nimble.lupin.pu_manager.utils.Constants
import retrofit2.Call
import retrofit2.Response


class CompletedTaskFragment : ReportBaseFragment(){
    override fun getTaskList() {
        handleApiResult(apiService.getScheduleTask(Constants.AdminWorkStation_ID,page, ""))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("sachin","Calling By onviewcreated")
        getTaskList()
    }

}
