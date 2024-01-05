package com.nimble.lupin.pu_manager.views.home.report

import android.os.Bundle
import android.view.View
import com.nimble.lupin.pu_manager.utils.Constants


class TaskNotYetStartedFragment : ReportBaseFragment(){
    override fun getTaskList() {
        handleApiResult(apiService.getScheduleTask(Constants.AdminWorkStation_ID,page, ""))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTaskList()
    }
}