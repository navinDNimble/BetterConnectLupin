package com.nimble.lupin.admin.models



data class TaskCreateResponseModel(
    val activities : List<ActivityModel>,
   val subActivities : List<SubActivityModel>,
   val taskModes : List<TaskModeModel>,
)
