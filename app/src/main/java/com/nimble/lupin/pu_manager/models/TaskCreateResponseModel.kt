package com.nimble.lupin.pu_manager.models



data class TaskCreateResponseModel(
    val activities : List<ActivityModel>,
   val subActivities : List<SubActivityModel>,
   val taskModes : List<TaskModeModel>,
)
